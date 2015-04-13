package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.targeting.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.time.buff.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.aptitude.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.common.debug.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.trigger.*;

public abstract class WakfuRunningEffect extends RunningEffect<WakfuEffect, WakfuEffectContainer>
{
    private static final int SIMULATE_SPELL_ID = 2040;
    private static final int SIMULATE_STATE_ID = 1793;
    private static final int MISSIZ_FRIZ_REANIMATION_STATE_ID = 1793;
    private int m_actionUid;
    private byte m_executionStatus;
    private int m_triggeringActionUniqueId;
    private RunningEffect m_triggeringEffectForCriterion;
    private boolean m_notified;
    protected RelativeFightTime m_endTime;
    protected long m_remainingTimeInMs;
    private boolean m_notifyUnapplicationForced;
    private EffectTargetsComputer m_effectTargetsComputer;
    private boolean m_notifyForced;
    protected boolean m_executed;
    private final BinarSerialPart GAME_SPECIFIC;
    private final BinarSerialPart EFFECT_CONTAINER;
    
    protected WakfuRunningEffect() {
        super();
        this.m_executionStatus = -1;
        this.m_endTime = RelativeFightTime.never();
        this.m_remainingTimeInMs = -1L;
        this.m_notifyUnapplicationForced = false;
        this.m_effectTargetsComputer = new EffectTargetsComputer(this);
        this.m_executed = false;
        this.GAME_SPECIFIC = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(WakfuRunningEffect.this.m_notifyUnapplicationForced ? 1 : 0));
                buffer.put(WakfuRunningEffect.this.m_executionStatus);
                WakfuRunningEffect.this.m_endTime.serialize(buffer);
                final long remainingTimeInMs = WakfuRunningEffect.this.getRemainingTimeInMs();
                buffer.putLong(remainingTimeInMs);
                buffer.put((byte)(WakfuRunningEffect.this.m_executed ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                WakfuRunningEffect.this.m_notifyUnapplicationForced = (buffer.get() == 1);
                WakfuRunningEffect.this.m_executionStatus = buffer.get();
                WakfuRunningEffect.this.m_endTime = RelativeFightTime.deserialize(buffer);
                WakfuRunningEffect.this.m_remainingTimeInMs = buffer.getLong();
                WakfuRunningEffect.this.m_executed = (buffer.get() == 1);
            }
            
            @Override
            public int expectedSize() {
                if (WakfuRunningEffect.this.m_endTime == null) {
                    WakfuRunningEffect.this.m_endTime = RelativeFightTime.never();
                    WakfuRunningEffect$1.m_logger.error((Object)(((RunningEffect<WakfuEffect, EC>)WakfuRunningEffect.this).getGenericEffect().getActionId() + " : m_endtime null, th\u00e9oriquement impossible"));
                }
                final int n = 2;
                final RelativeFightTime endTime = WakfuRunningEffect.this.m_endTime;
                return n + RelativeFightTime.serializedSize() + 8 + 1;
            }
        };
        this.EFFECT_CONTAINER = new BinarSerialPart(12) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                if (WakfuRunningEffect.this.m_effectContainer != null) {
                    buffer.putInt(((WakfuEffectContainer)WakfuRunningEffect.this.m_effectContainer).getContainerType());
                    buffer.putLong(((WakfuEffectContainer)WakfuRunningEffect.this.m_effectContainer).getEffectContainerId());
                }
                else {
                    buffer.putInt(0);
                    buffer.putLong(0L);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final int type = buffer.getInt();
                final long id = buffer.getLong();
                switch (type) {
                    case 11:
                    case 25: {
                        final int spellId = AbstractSpellLevel.getSpellIdFromDefautUId(id);
                        final short level = AbstractSpellLevel.getLevelFromDefautUId(id);
                        final AbstractSpellLevel spellLevel = AbstractSpellManager.getInstance().getDefaultSpellLevel(spellId, level);
                        ((RunningEffect<FX, AbstractSpellLevel>)WakfuRunningEffect.this).setEffectContainer(spellLevel);
                        break;
                    }
                    case 1: {
                        if (((RunningEffect<FX, WakfuEffectContainer>)WakfuRunningEffect.this).getEffectContainer() != null) {
                            break;
                        }
                        boolean containerFound = WakfuRunningEffect.this.setStateContainerFrom(WakfuRunningEffect.this.getTarget(), id);
                        if (!containerFound) {
                            containerFound = WakfuRunningEffect.this.setStateContainerFrom(WakfuRunningEffect.this.getCaster(), id);
                        }
                        if (!containerFound) {
                            containerFound = WakfuRunningEffect.this.createStateContainer(id);
                        }
                        if (!containerFound) {
                            WakfuRunningEffect$2.m_logger.error((Object)("Impossible de cr\u00e9er un \u00e9tat en tant que container d'un effet, id unique du container : " + id));
                            break;
                        }
                        break;
                    }
                    case 3: {
                        if (WakfuRunningEffect.this.m_context != null && WakfuRunningEffect.this.m_context.getEffectAreaManager() != null) {
                            final AbstractEffectArea area = (AbstractEffectArea)WakfuRunningEffect.this.m_context.getEffectAreaManager().getEffectAreaWithId(id);
                            if (area != null) {
                                ((RunningEffect<FX, AbstractEffectArea>)WakfuRunningEffect.this).setEffectContainer(area);
                            }
                            else {
                                WakfuRunningEffect$2.m_logger.error((Object)("Area inexistante " + id + " sur le contexte de type " + WakfuRunningEffect.this.m_context.getContextType()));
                            }
                            break;
                        }
                        WakfuRunningEffect$2.m_logger.error((Object)"contexte non ou mal initialis\u00e9");
                        break;
                    }
                    case 12: {
                        final Item defaultItem = ReferenceItemManager.getInstance().getDefaultItem((int)id);
                        if (defaultItem != null) {
                            ((RunningEffect<FX, Item>)WakfuRunningEffect.this).setEffectContainer(defaultItem);
                            break;
                        }
                        WakfuRunningEffect$2.m_logger.error((Object)("item par d\u00e9faut inconnu, referenceId = " + id));
                        break;
                    }
                    case 14:
                    case 31: {
                        WakfuRunningEffect$2.m_logger.error((Object)"On ne devrait pas s\u00e9rialiser les effets des set ou de sac");
                        break;
                    }
                    case 17: {
                        if (!(WakfuRunningEffect.this.m_caster instanceof BasicCharacterInfo)) {
                            break;
                        }
                        final BasicCharacterInfo caster = (BasicCharacterInfo)WakfuRunningEffect.this.m_caster;
                        final AptitudeInventory aptitudeInventory = caster.getAptitudeInventory();
                        if (aptitudeInventory == null) {
                            WakfuRunningEffect$2.m_logger.error((Object)("[WRE] On re\u00e7oit un effet (id=" + WakfuRunningEffect.this.getId() + ") d'aptitude serialis\u00e9 mais le caster n'a pas d'inventaire d'aptitude (inutile au serveur d'IA) caster : " + WakfuRunningEffect.this.m_caster));
                            return;
                        }
                        if (aptitudeInventory == EmptyAptitudeInventory.INSTANCE) {
                            return;
                        }
                        final List<Aptitude> aptitudeList = aptitudeInventory.getAllWithReferenceId((int)id);
                        if (aptitudeList == null || aptitudeList.isEmpty()) {
                            WakfuRunningEffect$2.m_logger.error((Object)"[WRE] On ne connait pas l'aptitude qui contient l'effet deserialise");
                            return;
                        }
                        if (aptitudeList.size() > 1) {
                            WakfuRunningEffect$2.m_logger.error((Object)"[WRE] On recupere plusieurs aptitudes pour un meme id de reference, ce n'est pas logique, on ne peut pas donner de container a notre effet");
                            return;
                        }
                        ((RunningEffect<FX, Aptitude>)WakfuRunningEffect.this).setEffectContainer(aptitudeList.get(0));
                        break;
                    }
                    case 21: {
                        ((RunningEffect<FX, WakfuEffectContainer>)WakfuRunningEffect.this).setEffectContainer(TimelineBuffListManager.getContainerForEffect(id, ((RunningEffect<WakfuEffect, EC>)WakfuRunningEffect.this).getGenericEffect()));
                        break;
                    }
                    case 28: {
                        ((RunningEffect<FX, WakfuEffectContainer>)WakfuRunningEffect.this).setEffectContainer(EffectContainerConstants.HAVEN_WORLD_CONTAINER);
                        break;
                    }
                    case 32: {
                        ((RunningEffect<FX, WakfuEffectContainer>)WakfuRunningEffect.this).setEffectContainer(EffectContainerConstants.GUILD_CONTAINER);
                        break;
                    }
                    case 30: {
                        ((RunningEffect<FX, WakfuEffectContainer>)WakfuRunningEffect.this).setEffectContainer(EffectContainerConstants.ACHIEVEMENT_BONUS_CONTAINER);
                        break;
                    }
                    case 36: {
                        final int refId = WakfuEffectContainerUtils.getRefIdFromDefaultUid(id);
                        final short aptLevel = WakfuEffectContainerUtils.getLevelFromDefaultUid(id);
                        final AptitudeBonusModel bonusModel = AptitudeBonusModelManager.INSTANCE.get(refId);
                        if (bonusModel != null) {
                            ((RunningEffect<FX, AptitudeBonusEffectContainer>)WakfuRunningEffect.this).setEffectContainer(new AptitudeBonusEffectContainer(bonusModel, aptLevel));
                            break;
                        }
                        break;
                    }
                    case 26:
                    case 29:
                    case 33: {
                        final WakfuEffectContainerBuilder containerBuilder = new WakfuEffectContainerBuilder();
                        containerBuilder.setContainerType(type).setContainerId(id);
                        ((RunningEffect<FX, WakfuEffectContainer>)WakfuRunningEffect.this).setEffectContainer(containerBuilder.build());
                        break;
                    }
                }
            }
        };
    }
    
    public short getContainerLevel() {
        if (this.getParams() != null) {
            final int forcedLevel = ((WakfuEffectExecutionParameters)this.getParams()).getForcedLevel();
            if (forcedLevel != -1) {
                return (short)forcedLevel;
            }
        }
        if (((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer() == null) {
            return 0;
        }
        return ((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer().getLevel();
    }
    
    public void setNotifyForced(final boolean notifyForced) {
        this.m_notifyForced = notifyForced;
    }
    
    public void setNotified() {
        this.m_notified = true;
    }
    
    public void setNotifyUnapplicationForced(final boolean notifyUnapplicationForced) {
        this.m_notifyUnapplicationForced = notifyUnapplicationForced;
    }
    
    public boolean isForcedUnapplicationNotify() {
        return this.m_notifyUnapplicationForced;
    }
    
    @Override
    public List<List<EffectUser>> determineTargets(final WakfuEffect genericEffect, final Target applicant, final EffectContext<WakfuEffect> context, final int targetCellx, final int targetCelly, final short targetCellz) {
        return this.m_effectTargetsComputer.determineTargets(genericEffect, applicant, context, targetCellx, targetCelly, targetCellz);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_actionUid = FightActionUniqueIDGenerator.getNextID();
        this.m_triggeringActionUniqueId = -1;
        this.m_value = 0;
        this.m_notified = false;
        this.m_remainingTimeInMs = -1L;
        this.m_executed = false;
        this.m_notifyForced = false;
        this.m_notifyUnapplicationForced = false;
        this.m_endTime = RelativeFightTime.never();
        this.m_startTime = AbsoluteFightTime.never();
        this.m_executionStatus = -1;
        this.m_effectTargetsComputer = new EffectTargetsComputer(this);
        PoolDebug.add(this);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_value = 0;
        this.m_endTime = RelativeFightTime.never();
        this.m_notifyUnapplicationForced = false;
        this.m_effectTargetsComputer = null;
        PoolDebug.remove(this);
    }
    
    public boolean isNotifyForced() {
        return this.m_notifyForced;
    }
    
    public boolean hasProperty(final RunningEffectPropertyType property) {
        return this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).hasProperty(property);
    }
    
    public void forceImmediateNotifyExecution(final RunningEffect triggerRE, final boolean trigger) {
        this.m_notifyForced = true;
        this.notifyExecution(triggerRE, trigger);
    }
    
    public void notifyExecution(final RunningEffect triggerRE, final boolean trigger) {
        if (this.doNotNotify() || this.m_notified) {
            return;
        }
        this.m_notified = true;
        if (this.m_context != null && this.m_context.getEffectExecutionListener() != null && !trigger) {
            this.m_context.getEffectExecutionListener().onEffectDirectExecution(this);
        }
        if (triggerRE != null) {
            this.m_triggeringActionUniqueId = ((WakfuRunningEffect)triggerRE).getActionUid();
        }
        else {
            this.m_triggeringActionUniqueId = -1;
        }
        if (this.m_context != null && this.m_context.getEffectExecutionListener() != null && trigger) {
            this.m_context.getEffectExecutionListener().onEffectTriggeredExecution(this);
        }
    }
    
    private boolean doNotNotify() {
        final WakfuEffectExecutionParameters params = (WakfuEffectExecutionParameters)this.getParams();
        return (params != null && params.isDoNotNotify()) || (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).doNotNotify());
    }
    
    public void setNotified(final boolean alreadyNotify) {
        this.m_notified = alreadyNotify;
    }
    
    public boolean isNotified() {
        return this.m_notified;
    }
    
    @Override
    public void askForExecution() {
        if (this.getParent() == null && this.getParams() != null && ((WakfuEffectExecutionParameters)this.getParams()).getExternalTriggeringEffect() != null) {
            this.setParent(((WakfuEffectExecutionParameters)this.getParams()).getExternalTriggeringEffect());
        }
        super.askForExecution();
    }
    
    @Override
    public void askForTriggeredUnapplication() {
        this.setNotifyUnapplicationForced(true);
        super.askForTriggeredUnapplication();
    }
    
    @Override
    public final void computeValue(final RunningEffect triggerRE) {
        this.effectiveComputeValue(triggerRE);
        final WakfuEffectExecutionParameters params = (WakfuEffectExecutionParameters)this.getParams();
        this.notifyComputeValue();
        if (params == null || !params.isValueForced()) {
            return;
        }
        this.modifyValue(params);
    }
    
    private void notifyComputeValue() {
        final EffectExecutionParameters params = this.getParams();
        if (params == null || !(params instanceof WakfuEffectExecutionParameters)) {
            return;
        }
        final List<WakfuRunningEffectListener> listeners = ((WakfuEffectExecutionParameters)params).getListeners();
        if (listeners == null || listeners.isEmpty()) {
            return;
        }
        for (final WakfuRunningEffectListener listener : listeners) {
            try {
                listener.valueComputed(this);
            }
            catch (Exception e) {
                WakfuRunningEffect.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    private void modifyValue(final WakfuEffectExecutionParameters params) {
        switch (params.getValueForcedType()) {
            case SET: {
                this.forceValue(params.getForcedValue());
                break;
            }
            case PERCENT: {
                this.forceValue(Math.max(this.getValue() * params.getForcedValue() / 100, 1));
                break;
            }
        }
    }
    
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void afterExecutionTriggerHook() {
        super.afterExecutionTriggerHook();
        final EffectExecutionParameters params = this.getParams();
        if (params == null || !(params instanceof WakfuEffectExecutionParameters)) {
            return;
        }
        final List<WakfuRunningEffectListener> listeners = ((WakfuEffectExecutionParameters)params).getListeners();
        if (listeners == null || listeners.isEmpty()) {
            return;
        }
        for (final WakfuRunningEffectListener listener : listeners) {
            try {
                listener.onAfterExecution(this);
            }
            catch (Exception e) {
                WakfuRunningEffect.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    public final void execute(final RunningEffect triggerRE, final boolean trigger) {
        try {
            this.executeOverride(triggerRE, trigger);
        }
        catch (Exception e) {
            WakfuRunningEffect.m_logger.error((Object)("Exception lors de l'execution d'un effet " + ((this.m_genericEffect != null) ? ((WakfuEffect)this.m_genericEffect).getEffectId() : this.getId())), (Throwable)e);
        }
        this.m_executed = true;
        if (!this.m_notified) {
            try {
                this.notifyExecution(triggerRE, trigger);
            }
            catch (Exception e) {
                WakfuRunningEffect.m_logger.error((Object)"Exception levee lors de la notification de l'execution", (Throwable)e);
            }
        }
        this.m_notified = false;
        super.execute(triggerRE, trigger);
    }
    
    protected abstract void executeOverride(final RunningEffect p0, final boolean p1);
    
    @Override
    public boolean dontTriggerAnything() {
        return super.dontTriggerAnything() || (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).hasProperty(RunningEffectPropertyType.DONT_TRIGGER_ANYTHING_LEVEL_1));
    }
    
    @Override
    public boolean trigger(final byte triggerType) {
        boolean somethingWasTriggered = false;
        try {
            somethingWasTriggered |= super.trigger(triggerType);
            if (this.m_context == null || !(this.m_context instanceof WakfuFightEffectContext)) {
                return somethingWasTriggered;
            }
            byte globalTriggerType = 0;
            switch (triggerType) {
                case 3: {
                    globalTriggerType = 31;
                    break;
                }
                case 1: {
                    globalTriggerType = 11;
                    break;
                }
                case 2: {
                    globalTriggerType = 21;
                    break;
                }
                case 4: {
                    globalTriggerType = 41;
                    break;
                }
                case 6: {
                    globalTriggerType = 61;
                    break;
                }
                default: {
                    WakfuRunningEffect.m_logger.error((Object)("Pas de type de trigger global correspondant " + triggerType));
                    return somethingWasTriggered;
                }
            }
            final Iterator<EffectUser> allPossibleTargets = this.m_context.getTargetInformationProvider().getAllPossibleTargets();
            while (allPossibleTargets.hasNext()) {
                final EffectUser effectUser = allPossibleTargets.next();
                somethingWasTriggered |= ((Triggerable<WakfuRunningEffect>)effectUser).trigger(this.getTriggersToExecute(), this, globalTriggerType);
            }
        }
        catch (Exception e) {
            WakfuRunningEffect.m_logger.error((Object)"Exception catch\u00e9e : ", (Throwable)e);
        }
        return somethingWasTriggered;
    }
    
    @Override
    protected EffectUser getTriggeringTarget(final RunningEffect linkedRE) {
        if (linkedRE == null || this.m_genericEffect == null) {
            return this.m_target;
        }
        final TriggerTargetType triggerTargetType = ((WakfuEffect)this.m_genericEffect).getTriggerTargetType();
        try {
            switch (triggerTargetType) {
                case NONE: {
                    WakfuRunningEffect.m_logger.error((Object)("Pas de type de cibles sp\u00e9cifi\u00e9e pour un effet d\u00e9clench\u00e9, on retrourne la cible originale, id : " + ((WakfuEffect)this.m_genericEffect).getEffectId()));
                    return this.m_target;
                }
                case EFFECT_ORIGINAL_TARGET: {
                    return this.m_target;
                }
                case EFFECT_CARRIER: {
                    return this.getManagerWhereIamStored().getOwner();
                }
                case EFFECT_CASTER: {
                    return this.m_caster;
                }
                case TRIGGERING_EFFECT_CASTER: {
                    return linkedRE.getCaster();
                }
                case TRIGGERING_EFFECT_TARGET: {
                    return linkedRE.getTarget();
                }
                default: {
                    WakfuRunningEffect.m_logger.error((Object)("Type de cible d'effet declench\u00e9 inconnue " + triggerTargetType));
                    return this.m_target;
                }
            }
        }
        catch (Exception e) {
            WakfuRunningEffect.m_logger.error((Object)("Erreur lors de la r\u00e9cup\u00e9ration du triggering target sur l'effet " + ((WakfuEffect)this.m_genericEffect).getEffectId()), (Throwable)e);
            return null;
        }
    }
    
    @Override
    protected EffectUser getTriggeringCaster(final RunningEffect linkedRE) {
        if (this.m_genericEffect == null || linkedRE == null) {
            return this.m_caster;
        }
        final TriggerCasterType casterType = ((WakfuEffect)this.m_genericEffect).getTriggerCasterType();
        if (casterType == null) {
            return this.m_caster;
        }
        try {
            switch (casterType) {
                case NONE: {
                    WakfuRunningEffect.m_logger.error((Object)("Pas de type de caster sp\u00e9cifi\u00e9 pour un effet d\u00e9clench\u00e9, on retourne la cible originale, id : " + ((WakfuEffect)this.m_genericEffect).getEffectId()));
                    return this.m_caster;
                }
                case EFFECT_ORIGINAL_CASTER: {
                    return this.m_caster;
                }
                case EFFECT_CARRIER: {
                    return this.getManagerWhereIamStored().getOwner();
                }
                case EFFECT_TARGET: {
                    return this.m_target;
                }
                case TRIGGERING_EFFECT_CASTER: {
                    return linkedRE.getCaster();
                }
                case TRIGGERING_EFFECT_TARGET: {
                    return linkedRE.getTarget();
                }
                default: {
                    WakfuRunningEffect.m_logger.error((Object)("Type de cible d'effet declench\u00e9 inconnue " + casterType));
                    return this.m_caster;
                }
            }
        }
        catch (Exception e) {
            WakfuRunningEffect.m_logger.error((Object)("Erreur lors de la r\u00e9cup\u00e9ration du triggering caster sur l'effet " + ((WakfuEffect)this.m_genericEffect).getEffectId()), (Throwable)e);
            return null;
        }
    }
    
    @Override
    protected boolean checkEffectUserPlayChange() {
        boolean changed = super.checkEffectUserPlayChange();
        if (this.m_target != null && !this.m_target.isOutOfPlay() && this.m_target.mustGoOutOfPlay() && this.m_target.canChangePlayStatus()) {
            this.m_target.setUnderChange(true);
            changed = true;
            this.m_target.goOutOfPlay(this.m_caster);
            if (this.m_target != null) {
                this.m_target.setUnderChange(false);
            }
        }
        if (this.m_target != null && this.m_target.isOffPlay() && this.m_target.mustGoBackInPlay() && this.m_target.canChangePlayStatus()) {
            this.m_target.setUnderChange(true);
            changed = true;
            this.m_target.goBackInPlay(this.m_caster);
            if (this.m_target != null) {
                this.m_target.setUnderChange(false);
            }
        }
        return changed;
    }
    
    protected Long getIdOfFighterToToAttachTo() {
        if (this.m_genericEffect != null) {
            if (((WakfuEffect)this.m_genericEffect).isDurationInCasterTurn() && this.m_caster != null) {
                return this.m_caster.getId();
            }
            if (((WakfuEffect)this.m_genericEffect).isDurationInTargetTurn() && this.m_target != null) {
                return this.m_target.getId();
            }
        }
        if (this.m_target != null) {
            return this.m_target.getId();
        }
        if (this.m_caster != null) {
            return this.m_caster.getId();
        }
        return null;
    }
    
    @Override
    public void pushRunningEffectDelayedTimeEventInTimeline() {
        if (this.m_effectContainer == null) {
            return;
        }
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getEffectType() != 2) {
            return;
        }
        if (this.m_context.getTimeline() == null) {
            return;
        }
        if (!this.canAttachToTimeline()) {
            return;
        }
        final WakfuFightEffect effect = (WakfuFightEffect)this.m_genericEffect;
        this.m_context.getTimeline().addTimeEvent(new RunningEffectActivationEvent(this, this.getIdOfFighterToToAttachTo()), effect.getDelay(this.getContainerLevel()));
    }
    
    private boolean canAttachToTimeline() {
        if (this.getIdOfFighterToToAttachTo() == null) {
            WakfuRunningEffect.m_logger.error((Object)("[DESIGN EFFECT] Pas de point d'attache dans la timeline (#" + this.getUniqueId() + ' ' + this.actionAndGenericEffectIdString() + ')'));
            return false;
        }
        return true;
    }
    
    @Override
    public void pushRunningEffectDurationTimeEventInTimeline() {
        if (this.m_genericEffect == null || ((WakfuEffect)this.m_genericEffect).getEffectType() != 2 || this.m_context.getTimeline() == null) {
            return;
        }
        final WakfuFightEffect effect = (WakfuFightEffect)this.m_genericEffect;
        this.pushDeactivationTimeEventAtTime(effect.getDuration(this.getContainerLevel()));
    }
    
    @Override
    public AbsoluteFightTime getStartTime() {
        return this.m_startTime;
    }
    
    public void pushRunningEffectDurationTimeEventInTimelineAfterDelay() {
        if (!this.hasExecutionDelay()) {
            this.pushRunningEffectDurationTimeEventInTimeline();
            return;
        }
        final WakfuFightEffect effect = (WakfuFightEffect)this.m_genericEffect;
        final RelativeFightTimeInterval duration = effect.getDuration(this.getContainerLevel());
        int turn = duration.getTableTurnsFromNow() - effect.getDelay(this.getContainerLevel()).getTableTurnsFromNow();
        if (duration.isAtEndOfTurn()) {
            ++turn;
        }
        duration.setTableTurnsFromNow(turn);
        this.pushDeactivationTimeEventAtTime(duration);
    }
    
    protected void pushDeactivationTimeEventAtTime(final RelativeFightTimeInterval timeToDeactivation) {
        if (timeToDeactivation.isImmediate()) {
            return;
        }
        if (!this.canAttachToTimeline()) {
            return;
        }
        final TurnBasedTimeline timeline = this.m_context.getTimeline();
        this.m_startTime = timeline.now();
        this.m_endTime = timeline.addTimeEvent(new RunningEffectDeactivationEvent(this, this.getIdOfFighterToToAttachTo()), timeToDeactivation);
    }
    
    @Override
    public RelativeFightTime getEndTime() {
        return this.m_endTime;
    }
    
    public long getRemainingTimeInMs() {
        if (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getEffectType() == 1 && this.m_remainingTimeInMs < 0L) {
            this.m_remainingTimeInMs = ((RunningEffect<WakfuWorldEffect, EC>)this).getGenericEffect().getDurationInMs(this.getContainerLevel());
        }
        return this.m_remainingTimeInMs;
    }
    
    public void setRemainingTimeInMs(final long remainingTimeInMs) {
        this.m_remainingTimeInMs = remainingTimeInMs;
    }
    
    @Override
    public boolean hasDuration() {
        if (this.m_forceInstant) {
            return false;
        }
        if (this.isInfinite()) {
            return true;
        }
        if (this.m_genericEffect != null) {
            switch (((WakfuEffect)this.m_genericEffect).getEffectType()) {
                case 2: {
                    final WakfuFightEffect effect = (WakfuFightEffect)this.m_genericEffect;
                    final RelativeFightTimeInterval effectDuration = effect.getDuration(this.getContainerLevel());
                    return super.hasDuration() || (effectDuration != null && !effectDuration.isImmediate());
                }
                case 1: {
                    final WakfuWorldEffect effect2 = (WakfuWorldEffect)this.m_genericEffect;
                    return effect2.getDurationInMs(this.getContainerLevel()) > 0;
                }
                case 0: {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean hasDurationInMs() {
        return ((RunningEffect<WakfuEffect, EC>)this).getGenericEffect() == null || ((RunningEffect<WakfuEffect, EC>)this).getGenericEffect().getEffectType() == 1;
    }
    
    @Override
    public boolean isInfinite() {
        if (this.m_genericEffect == null) {
            return false;
        }
        final short effectContainerLevel = this.getContainerLevel();
        switch (((WakfuEffect)this.m_genericEffect).getEffectType()) {
            case 2: {
                final WakfuFightEffect effect = (WakfuFightEffect)this.m_genericEffect;
                final RelativeFightTimeInterval effectDuration = effect.getDuration(effectContainerLevel);
                return effectDuration != null && effectDuration.getTableTurnsFromNow() < 0;
            }
            case 1: {
                final WakfuWorldEffect effect2 = (WakfuWorldEffect)this.m_genericEffect;
                return effect2.getDurationInMs(effectContainerLevel) < 0;
            }
            case 0: {
                return true;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public boolean hasExecutionDelay() {
        return this.m_genericEffect != null && this.m_effectContainer != null && ((WakfuEffect)this.m_genericEffect).getEffectType() == 2 && !((WakfuFightEffect)this.m_genericEffect).getDelay(this.getContainerLevel()).isImmediate();
    }
    
    public Elements getElement() {
        return null;
    }
    
    public void addTriggersToExecute() {
        Elements element = this.getElement();
        if (element == null) {
            element = this.getSpellElement();
        }
        if (element == null) {
            return;
        }
        this.m_triggers.set(element.getEffectTrigger());
    }
    
    protected Elements getSpellElement() {
        if (((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer() == null) {
            return null;
        }
        final int containerType = ((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer().getContainerType();
        if (containerType == 11) {
            return ((RunningEffect<FX, AbstractSpellLevel>)this).getEffectContainer().getElement();
        }
        return null;
    }
    
    public int getActionUid() {
        return this.m_actionUid;
    }
    
    public boolean isExecuted() {
        return this.m_executed;
    }
    
    public int getTriggeringActionUniqueId() {
        return this.m_triggeringActionUniqueId;
    }
    
    @Override
    public boolean canBeExecuted() {
        if (this.m_genericEffect != null && !((WakfuEffect)this.m_genericEffect).isUsableInWorld() && !this.canBeExecutedOnKO() && this.m_target != null && this.m_target.isOffPlay()) {
            return false;
        }
        if (this.checkIsNotValidTargetProperty()) {
            return false;
        }
        if (this.m_target != null && !this.m_target.isValidForEffectExecution()) {
            return false;
        }
        if (this.m_genericEffect == null) {
            return true;
        }
        if (!this.isValidForContainerLevel()) {
            return false;
        }
        final WakfuEffectExecutionParameters executionParameters = (WakfuEffectExecutionParameters)this.getParams();
        if ((executionParameters == null || !executionParameters.disableCriterionCheck()) && !this.checkConditions(null)) {
            return false;
        }
        if (executionParameters != null && executionParameters.isExecutionDisabled()) {
            return false;
        }
        final short level = this.getContainerLevel();
        boolean roll = true;
        if (this.getParams() == null || !((WakfuEffectExecutionParameters)this.getParams()).disableProbabilityComputation()) {
            roll = (DiceRoll.roll(100) <= ValueRounder.randomRound(((WakfuEffect)this.m_genericEffect).getExecutionProbability(level)));
        }
        return roll;
    }
    
    protected boolean checkIsNotValidTargetProperty() {
        return false;
    }
    
    protected boolean isNotValidTargetAndContainerNotSpecialState() {
        return (!(this.m_effectContainer instanceof State) || ((State)this.m_effectContainer).getStatePowerType() != StatePowerType.SPECIAL) && this.m_target != null && this.m_target.isActiveProperty(FightPropertyType.IS_NOT_VALID_TARGET);
    }
    
    private boolean isValidForContainerLevel() {
        final short level = this.getContainerLevel();
        final int minLevel = ((WakfuEffect)this.m_genericEffect).getContainerMinLevel();
        final int maxLevel = ((WakfuEffect)this.m_genericEffect).getContainerMaxLevel();
        return level >= minLevel && level <= maxLevel;
    }
    
    @Override
    public boolean canBeTriggeredExecuted(final RunningEffect linkedRE) {
        if (!this.canBeExecutedOnKO() && this.m_target != null && this.m_target.isOffPlay()) {
            return false;
        }
        if (this.m_genericEffect == null) {
            return true;
        }
        if (!this.checkConditions(linkedRE)) {
            return false;
        }
        if (!this.isValidForContainerLevel()) {
            return false;
        }
        final short level = this.getContainerLevel();
        return DiceRoll.roll(100) <= ValueRounder.randomRound(((WakfuEffect)this.m_genericEffect).getExecutionProbability(level));
    }
    
    boolean checkConditions(final RunningEffect linkedRE) {
        final EffectUser caster = this.mustBeTriggered() ? this.getTriggeringCaster(linkedRE) : this.m_caster;
        final EffectUser target = this.mustBeTriggered() ? this.getTriggeringTarget(linkedRE) : this.m_target;
        this.m_triggeringEffectForCriterion = linkedRE;
        boolean valid;
        try {
            valid = (((WakfuEffect)this.m_genericEffect).getConditions() == null || ((WakfuEffect)this.m_genericEffect).getConditions().isValid(caster, (target == null) ? this.m_targetCell : target, this, this.m_context));
        }
        catch (Exception e) {
            WakfuRunningEffect.m_logger.error((Object)"Exception levee", (Throwable)e);
            valid = false;
        }
        finally {
            this.m_triggeringEffectForCriterion = null;
        }
        return valid;
    }
    
    public RunningEffect getTriggeringEffectForCriterion() {
        return this.m_triggeringEffectForCriterion;
    }
    
    @Override
    protected boolean canBeExecutedOnKO() {
        return this.hasProperty(RunningEffectPropertyType.CAN_BE_EXECUTED_ON_KO) || this.forceExecutionOnKoSpecialCases() || (this.getListeningTriggesrNotRelatedToExecutions() != null && this.getListeningTriggesrNotRelatedToExecutions().get(1010));
    }
    
    private boolean forceExecutionOnKoSpecialCases() {
        final WakfuEffectContainer container = ((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer();
        if (container == null) {
            return false;
        }
        final int type = container.getContainerType();
        if (type == 25) {
            if (((AbstractSpellLevel)container).getSpellId() == 2040L) {
                return true;
            }
        }
        else if (type == 1) {
            final short stateId = ((State)container).getStateBaseId();
            if (stateId == 1793) {
                return true;
            }
            if (stateId == 1793) {
                return true;
            }
        }
        return false;
    }
    
    public byte getExecutionStatus() {
        return this.m_executionStatus;
    }
    
    public void setExecutionStatus(final byte executionStatus) {
        this.m_executionStatus = executionStatus;
    }
    
    @Override
    public Effect getDefaultEffect() {
        return DefaultEffect.getInstance();
    }
    
    @Override
    public BinarSerialPart getGameSpecificDatasBinarSerialPart() {
        return this.GAME_SPECIFIC;
    }
    
    @Override
    protected void cloneParameters(final RunningEffect<WakfuEffect, WakfuEffectContainer> re) {
        super.cloneParameters(re);
        this.m_executionStatus = ((WakfuRunningEffect)re).getExecutionStatus();
        this.m_endTime = re.getEndTime();
        this.m_notifyForced = ((WakfuRunningEffect)re).m_notifyForced;
    }
    
    @Override
    public BinarSerialPart getEffectContainerBinarSerialPart() {
        return this.EFFECT_CONTAINER;
    }
    
    private boolean createStateContainer(final long id) {
        final State state = StateManager.getInstance().getBasicStateFromUniqueId((int)id);
        if (state == null) {
            return false;
        }
        this.m_effectContainer = (EC)state.instanceAnother(State.getLevelFromUniqueId((int)id));
        return true;
    }
    
    public boolean setStateContainerFrom(final EffectUser stateHolder, final long stateId) {
        if (stateHolder == null) {
            return false;
        }
        final RunningEffectManager effectManager = stateHolder.getRunningEffectManager();
        if (effectManager == null) {
            return false;
        }
        for (final RunningEffect re : effectManager) {
            if (re.getId() != RunningEffectConstants.RUNNING_STATE.getId()) {
                continue;
            }
            final State state = ((StateRunningEffect)re).getState();
            if (state == null) {
                WakfuRunningEffect.m_logger.error((Object)("Un StateRunningEffect avec un etat inexistant ??? re.getId() = " + re.getId()));
            }
            else {
                if (state.getUniqueId() == stateId) {
                    ((RunningEffect<FX, State>)this).setEffectContainer(state);
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public boolean isItemEquip() {
        final WakfuEffect effect = ((RunningEffect<WakfuEffect, EC>)this).getGenericEffect();
        if (effect == null) {
            return false;
        }
        final WakfuEffectContainer effectContainer = ((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer();
        if (effectContainer == null) {
            return false;
        }
        final int containerType = effectContainer.getContainerType();
        return (containerType == 12 || containerType == 14 || containerType == 31) && !effect.isAnUsableEffect();
    }
    
    @Override
    public boolean equalsRunningEffectDespiteTarget(final RunningEffect re) {
        try {
            return re != null && re.getValue() == this.getValue() && (re.getTargetCell() == null || re.getTargetCell().equals(this.getTargetCell())) && re.getCaster() == this.getCaster() && re.getEffectContainer() == ((RunningEffect<FX, EffectContainer>)this).getEffectContainer() && re.getGenericEffect() == this.getGenericEffect() && re.getEndTime() == this.getEndTime() && re.getId() == this.getId() && re.mustBeExecutedNow() == this.mustBeExecutedNow();
        }
        catch (Exception e) {
            WakfuRunningEffect.m_logger.warn((Object)ExceptionFormatter.toString(e));
            return false;
        }
    }
    
    public boolean unapplicationMustBeNotified() {
        if (this.doNotNotify()) {
            return false;
        }
        if (this.m_context instanceof WakfuFightEffectContext) {
            final WakfuFightEffectContext fightEffectContext = (WakfuFightEffectContext)this.m_context;
            final AbstractFight fight = fightEffectContext.getFight();
            if (fight.getStatus() == AbstractFight.FightStatus.PLACEMENT) {
                return true;
            }
        }
        return this.mustBeTriggered() || this.hasExecutionDelay() || this.m_notifyUnapplicationForced;
    }
    
    public boolean mustNotifyUnapplicationToOutsiders() {
        return !this.doNotNotify() && ((this.hasDuration() && !this.isItemEquip()) || this.m_notifyUnapplicationForced);
    }
    
    @Override
    protected void initialiseExecutionCount() {
        if (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getMaximumExecutions() >= 0) {
            this.m_maxExecutionCount = (int)(((WakfuEffect)this.m_genericEffect).getMaximumExecutions() + this.getContainerLevel() * ((WakfuEffect)this.m_genericEffect).getMaxExecutionIncr());
        }
        else {
            this.m_maxExecutionCount = -1;
        }
    }
    
    public boolean isProtectorBuff() {
        return this.m_effectContainer != null && ((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 19;
    }
    
    public boolean isAptitudeEffect() {
        return this.m_effectContainer != null && (((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 17 || ((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 36);
    }
    
    public boolean isPassiveSpellEffect() {
        return this.m_effectContainer != null && ((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 25;
    }
    
    public boolean isGuildEffect() {
        return this.m_effectContainer != null && ((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 32;
    }
    
    public boolean isHavenWorldEffect() {
        return this.m_effectContainer != null && ((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 28;
    }
    
    public boolean isAntiAddictionEffect() {
        return this.m_effectContainer != null && ((WakfuEffectContainer)this.m_effectContainer).getContainerType() == 34;
    }
    
    public boolean comeFromTransmigrableState() {
        return ((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer() != null && this.getEffectContainer() instanceof State && ((RunningEffect<FX, State>)this).getEffectContainer().isTransmigrable();
    }
    
    public boolean isGlobalTriggerListener() {
        return this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).isGlobalTriggerListener();
    }
    
    protected RunningEffect getTriggeringEffect(final RunningEffect triggerRE) {
        RunningEffect triggeringEffect;
        if (triggerRE != null) {
            triggeringEffect = triggerRE;
        }
        else if (this.getParams() != null) {
            triggeringEffect = ((WakfuEffectExecutionParameters)this.getParams()).getExternalTriggeringEffect();
        }
        else {
            triggeringEffect = null;
        }
        return triggeringEffect;
    }
    
    public void releaseIfNeed() {
        if (this.getManagerWhereIamStored() == null && !this.isReleased()) {
            this.release();
        }
    }
}
