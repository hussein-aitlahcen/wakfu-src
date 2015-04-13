package com.ankamagames.wakfu.common.game.effect.runningEffect;

import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class ApplyState extends DynamicallyDefinedRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    public static final TIntArrayList UNDECURSABLE_STATE;
    protected int m_stateUniqueId;
    protected short m_stateId;
    protected short m_stateLevel;
    private boolean m_stateInamovable;
    private boolean m_bypassResistanceCheck;
    private boolean m_doNotReplaceStateAlreadyPresent;
    private boolean m_forNaturalState;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    public ApplyState() {
        super();
        this.m_stateUniqueId = 0;
        this.m_stateId = 0;
        this.m_stateLevel = -1;
        this.m_stateInamovable = false;
        this.m_bypassResistanceCheck = false;
        this.ADDITIONNAL_DATAS = new BinarSerialPart(4) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putShort(ApplyState.this.m_stateId);
                buffer.putShort(ApplyState.this.m_stateLevel);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                ApplyState.this.m_stateId = buffer.getShort();
                ApplyState.this.m_stateLevel = buffer.getShort();
                ApplyState.this.m_stateUniqueId = State.getUniqueIdFromBasicInformation(ApplyState.this.m_stateId, ApplyState.this.m_stateLevel);
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ApplyState.PARAMETERS_LIST_SET;
    }
    
    @Override
    public ApplyState newInstance() {
        ApplyState re;
        try {
            re = (ApplyState)ApplyState.m_staticPool.borrowObject();
            re.m_pool = ApplyState.m_staticPool;
        }
        catch (Exception e) {
            re = new ApplyState();
            re.m_isStatic = false;
            re.m_pool = null;
            ApplyState.m_logger.error((Object)("Erreur lors d'un newInstance sur un LatentState : " + e.getMessage()));
        }
        this.copyParams(re);
        return re;
    }
    
    protected void copyParams(final ApplyState re) {
        re.m_stateId = this.m_stateId;
        re.m_stateUniqueId = this.m_stateUniqueId;
        re.m_stateLevel = this.m_stateLevel;
    }
    
    @Override
    public void onCheckIn() {
        this.m_stateInamovable = false;
        this.m_stateUniqueId = 0;
        this.m_doNotReplaceStateAlreadyPresent = false;
        this.m_bypassResistanceCheck = false;
        this.m_forNaturalState = false;
        super.onCheckIn();
    }
    
    public static ApplyState checkout(final EffectContext<WakfuEffect> context, final EffectUser target, final short stateId, final short stateLevel, final boolean inamovable) {
        ApplyState re;
        try {
            re = (ApplyState)ApplyState.m_staticPool.borrowObject();
            re.m_pool = ApplyState.m_staticPool;
        }
        catch (Exception e) {
            re = new ApplyState();
            re.m_pool = null;
            re.m_isStatic = false;
            ApplyState.m_logger.error((Object)("Erreur lors d'un checkOut sur un ApplyState : " + e.getMessage()));
        }
        re.m_id = RunningEffectConstants.STATE_APPLY.getId();
        re.m_status = RunningEffectConstants.STATE_APPLY.getObject().getRunningEffectStatus();
        re.setTriggersToExecute();
        re.m_target = target;
        re.m_forceInstant = true;
        re.m_stateId = stateId;
        re.m_stateLevel = stateLevel;
        re.m_stateUniqueId = State.getUniqueIdFromBasicInformation(re.m_stateId, re.m_stateLevel);
        re.m_stateInamovable = inamovable;
        re.m_maxExecutionCount = -1;
        re.m_context = (EffectContext<FX>)context;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(2226);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target == null || this.m_target.getRunningEffectManager() == null) {
            this.setNotified(true);
            return;
        }
        if (this.m_caster == null) {
            this.setNotified(true);
            return;
        }
        this.setExecutionStatus((byte)5);
        final List<StateRunningEffect> toRemove = this.checkStateSuperposition();
        if (this.m_doNotReplaceStateAlreadyPresent) {
            return;
        }
        this.removePreviousStateEffects(toRemove);
        if (this.isValueComputationEnabled()) {
            this.notifyExecution(linkedRE, trigger);
            try {
                this.applyStateRunningEffect(linkedRE);
            }
            catch (Exception e) {
                ApplyState.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    public void update(final int whatToUpdate, final float howMuchToUpate, final boolean set) {
        switch (whatToUpdate) {
            case 4: {
                if (set) {
                    this.m_stateLevel = (short)howMuchToUpate;
                }
                else {
                    this.m_stateLevel += (short)howMuchToUpate;
                }
                this.m_stateUniqueId = State.getUniqueIdFromBasicInformation(this.m_stateId, this.m_stateLevel);
                break;
            }
        }
    }
    
    private boolean hasImmunizingState() {
        if (this.m_target == null) {
            return true;
        }
        final RunningEffectManager rem = this.m_target.getRunningEffectManager();
        if (rem == null) {
            ApplyState.m_logger.warn((Object)("On essaie d'appliquer un etat sur un cible qui n' pas de REM " + this.m_target + " , effet correspondant : " + this.getEffectId()));
            return false;
        }
        for (final RunningEffect effect : rem) {
            if (effect.getId() == RunningEffectConstants.RUNNING_STATE.getId() && ((StateRunningEffect)effect).getState().getStateImmunities().contains(this.m_stateId)) {
                this.setExecutionStatus((byte)2);
                return true;
            }
        }
        return false;
    }
    
    private List<StateRunningEffect> checkStateSuperposition() {
        this.m_stateId = State.getBasicIdFromUniqueId(this.m_stateUniqueId);
        this.m_stateLevel = State.getLevelFromUniqueId(this.m_stateUniqueId);
        final ArrayList<StateRunningEffect> toRemove = new ArrayList<StateRunningEffect>();
        for (final RunningEffect runningEffect : this.m_target.getRunningEffectManager()) {
            if (runningEffect == null) {
                continue;
            }
            if (runningEffect.getId() != RunningEffectConstants.RUNNING_STATE.getId()) {
                continue;
            }
            if (((StateRunningEffect)runningEffect).getState().getStateBaseId() != this.m_stateId) {
                continue;
            }
            final State state = ((StateRunningEffect)runningEffect).getState();
            if (state.isInamovable() && !state.isCumulable()) {
                this.setNotified(true);
                this.m_doNotReplaceStateAlreadyPresent = true;
                return Collections.emptyList();
            }
            if (state.isCumulable() && (state.getLevel() < state.getMaxlevel() || state.reapplyEvenAtMaxLevel())) {
                this.m_stateLevel += state.getLevel();
                this.m_stateUniqueId = State.getUniqueIdFromBasicInformation(this.m_stateId, this.m_stateLevel);
                toRemove.add((StateRunningEffect)runningEffect);
            }
            else {
                if (!state.isReplacable() || this.m_stateLevel < state.getLevel()) {
                    this.setNotified(true);
                    this.m_doNotReplaceStateAlreadyPresent = true;
                    return Collections.emptyList();
                }
                toRemove.add((StateRunningEffect)runningEffect);
            }
        }
        return toRemove;
    }
    
    private void removePreviousStateEffects(final List<StateRunningEffect> toRemove) {
        if (toRemove != null) {
            for (final StateRunningEffect effect : toRemove) {
                effect.setNotifyUnapplicationForced(true);
                this.m_target.getRunningEffectManager().removeEffect(effect);
            }
        }
    }
    
    private WakfuEffect getStateRunningGenericEffect() {
        if (((RunningEffect<WakfuEffect, EC>)this).getGenericEffect() == null) {
            return DefaultEffect.getInstance();
        }
        if (((RunningEffect<WakfuEffect, EC>)this).getGenericEffect().isUsableInWorld()) {
            if (((RunningEffect<WakfuEffect, EC>)this).getGenericEffect().isUsableInFight()) {
                return DefaultWorldAndFightUsableEffect.getInstance();
            }
            return DefaultWorldUsableEffect.getInstance();
        }
        else {
            if (((RunningEffect<WakfuEffect, EC>)this).getGenericEffect().isUsableInFight()) {
                return DefaultFightUsableEffect.getInstanceWithoutMaxLevel();
            }
            return DefaultEffect.getInstance();
        }
    }
    
    private void applyStateRunningEffect(final RunningEffect linkedRE) {
        this.m_stateUniqueId = State.getUniqueIdFromBasicInformation(this.m_stateId, this.m_stateLevel);
        final StateRunningEffect stateRunningEffect = StateRunningEffect.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_caster, (WakfuEffectContainer)this.m_effectContainer, this.m_stateUniqueId);
        if (this.m_stateInamovable) {
            stateRunningEffect.setInamovable();
        }
        if (ApplyState.UNDECURSABLE_STATE.contains(this.m_stateId)) {
            stateRunningEffect.setInamovable();
        }
        this.modifyStateRunningEffectIfNecessary(stateRunningEffect);
        final WakfuEffectExecutionParameters parameters = WakfuEffectExecutionParameters.checkOut(true, false, null);
        if (this.m_forNaturalState) {
            parameters.setDoNotNotify(true);
        }
        stateRunningEffect.setExecutionParameters(parameters);
        ((RunningEffect<FX, WakfuEffectContainer>)stateRunningEffect).setEffectContainer((WakfuEffectContainer)this.m_effectContainer);
        ((RunningEffect<WakfuEffect, EC>)stateRunningEffect).setGenericEffect(this.getStateRunningGenericEffect());
        stateRunningEffect.addEndTriggers(this.getListeningTriggerForUnapplication());
        stateRunningEffect.setParent(linkedRE);
        stateRunningEffect.forceDontTriggerAnything();
        if (this.isNotifyForced()) {
            stateRunningEffect.setNotifyForced(true);
        }
        stateRunningEffect.applyOnTargets(this.m_target);
        stateRunningEffect.release();
    }
    
    protected void modifyStateRunningEffectIfNecessary(final StateRunningEffect stateRunningEffect) {
    }
    
    protected int getApplicationProbability() {
        if (this.m_genericEffect == null) {
            return 100;
        }
        int applyPercent = this.getApplyPercent();
        if (applyPercent == -1) {
            return 100;
        }
        if (applyPercent == -2) {
            return 0;
        }
        final short stateId = (short)((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final State state = StateManager.getInstance().getState(stateId);
        if (state == null) {
            ApplyState.m_logger.error((Object)("UNable to check application probability for state " + stateId + " : this state is unknown"));
            return 0;
        }
        if (this.m_target instanceof BasicCharacterInfo) {
            applyPercent -= ((BasicCharacterInfo)this.m_target).getStateResistance(this.m_stateId);
        }
        if (this.m_caster instanceof BasicCharacterInfo) {
            applyPercent += ((BasicCharacterInfo)this.m_caster).getStateApplicationBonus(this.m_stateId);
        }
        final StateType stateType = state.getStateType();
        if (stateType == StateType.POSITIF) {
            return MathHelper.clamp(applyPercent, 0, 100);
        }
        float percentModificator = 0.0f;
        if (this.m_target != null && this.m_target.hasCharacteristic(FighterCharacteristicType.STATE_RESISTANCE_BONUS)) {
            percentModificator -= this.m_target.getCharacteristicValue(FighterCharacteristicType.STATE_RESISTANCE_BONUS);
        }
        if (this.m_caster != null && this.m_caster.hasCharacteristic(FighterCharacteristicType.STATE_APPLICATION_BONUS)) {
            percentModificator += this.m_caster.getCharacteristicValue(FighterCharacteristicType.STATE_APPLICATION_BONUS);
        }
        applyPercent += (int)(applyPercent * percentModificator / 100.0f);
        return MathHelper.clamp(applyPercent, 0, 100);
    }
    
    protected int getApplyPercent() {
        int basicProbability = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 6) {
            return basicProbability;
        }
        final RunningEffectManager rem = this.getConcernedManager();
        if (!(rem instanceof TimedRunningEffectManager)) {
            return basicProbability;
        }
        final TimedRunningEffectManager runningEffectManager = (TimedRunningEffectManager)rem;
        final int modifierStateId = ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final boolean applyIfStateNotPresent = ((WakfuEffect)this.m_genericEffect).getParam(4, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
        final float incrementByStateLevel = ((WakfuEffect)this.m_genericEffect).getParam(5, this.getContainerLevel());
        final StateRunningEffect runningState = runningEffectManager.getRunningState(modifierStateId);
        if (runningState != null) {
            if (basicProbability != -1) {
                final int modif = Math.round(incrementByStateLevel * runningState.getState().getLevel());
                basicProbability += modif;
            }
        }
        else if (!applyIfStateNotPresent) {
            return -2;
        }
        return basicProbability;
    }
    
    protected RunningEffectManager getConcernedManager() {
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() <= 6) {
            return this.m_caster.getRunningEffectManager();
        }
        final boolean checkOnTarget = ((WakfuEffect)this.m_genericEffect).getParam(6, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
        if (checkOnTarget) {
            return this.m_target.getRunningEffectManager();
        }
        return this.m_caster.getRunningEffectManager();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        final short level = this.getContainerLevel();
        this.m_stateId = (short)((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.extractStateLevel(level);
        this.m_stateUniqueId = State.getUniqueIdFromBasicInformation(this.m_stateId, this.m_stateLevel);
    }
    
    protected void extractStateLevel(final short level) {
        this.m_stateLevel = (short)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final int paramsCount = ((WakfuEffect)this.m_genericEffect).getParamsCount();
        if (paramsCount != 4 && paramsCount != 5) {
            return;
        }
        if (this.m_caster == null || !(this.m_caster instanceof BasicCharacterInfo)) {
            ApplyState.m_logger.error((Object)"Impossible d'appliquer l'etat au niveau param\u00e9tr\u00e9, le caster n'est pas valide");
            return;
        }
        final int spellId = ((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final SpellInventory<? extends AbstractSpellLevel> spellInventory = ((BasicCharacterInfo)this.m_caster).getSpellInventory();
        if (spellInventory == null) {
            ApplyState.m_logger.error((Object)"Impossible d'appliquer l'etat au niveau param\u00e9tr\u00e9, le caster n'a pas d'inventaire de sort");
            return;
        }
        final AbstractSpellLevel spell = spellInventory.getFirstWithReferenceId(spellId);
        if (spell == null) {
            ApplyState.m_logger.error((Object)("Impossible d'appliquer l'etat au niveau param\u00e9tr\u00e9, l'inventaire de sort ne contient pas le sort " + spellId + ", breed caster : " + ((BasicCharacterInfo)this.m_caster).getBreedId() + " effect id = " + this.getEffectId()));
            return;
        }
        this.m_stateLevel = spell.getLevel();
        if (paramsCount < 5) {
            return;
        }
        final float factor = ((WakfuEffect)this.m_genericEffect).getParam(4, level);
        this.m_stateLevel *= (short)factor;
    }
    
    @Override
    protected boolean checkIsNotValidTargetProperty() {
        final State state = StateManager.getInstance().getState(this.m_stateId);
        return (state == null || state.getStatePowerType() != StatePowerType.SPECIAL) && this.m_target != null && this.m_target.isActiveProperty(FightPropertyType.IS_NOT_VALID_TARGET);
    }
    
    @Override
    public boolean canBeExecuted() {
        if (this.isValueComputationEnabled()) {
            final State abstractstate = StateManager.getInstance().getBasicStateFromUniqueId(this.m_stateUniqueId);
            if (abstractstate != null && abstractstate.getApplyCriterions() != null && !abstractstate.getApplyCriterions().isValid(this.m_caster, this.m_target, ((RunningEffect<FX, Object>)this).getEffectContainer(), this.getContext())) {
                return false;
            }
            if (this.m_target != null && this.m_target.isActiveProperty(FightPropertyType.CANT_BE_STATE_TARGET) && abstractstate != null && abstractstate.getStatePowerType() != StatePowerType.SPECIAL) {
                return false;
            }
            if (this.m_stateId > 0) {
                if (this.hasImmunizingState()) {
                    return false;
                }
                if (!this.m_bypassResistanceCheck) {
                    final int applicationProbability = this.getApplicationProbability();
                    if (applicationProbability < 100) {
                        final int roll = DiceRoll.roll(100);
                        if (applicationProbability < roll) {
                            this.m_value = 0;
                            return false;
                        }
                    }
                }
            }
        }
        return super.canBeExecuted();
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    public short getStateId() {
        return this.m_stateId;
    }
    
    public short getStateLevel() {
        return this.m_stateLevel;
    }
    
    public void bypassResistancesCheck() {
        this.m_bypassResistanceCheck = true;
    }
    
    public void setForNaturalState(final boolean forNaturalState) {
        this.m_forNaturalState = forNaturalState;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ApplyState>() {
            @Override
            public ApplyState makeObject() {
                return new ApplyState();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("apply avec gestion des resistances/boosts", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("pourcentage d'application (-1 = application forc\u00e9e)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Level fct d'un autre sort", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level (Inutile)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("pourcentage d'application (-1 = application forc\u00e9e)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("id du sort", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Level fct d'un autre sort", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level (Inutile)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("pourcentage d'application (-1 = application forc\u00e9e)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("id du sort", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Multiplicateur", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Proba fct du level d'un autre \u00e9tat", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("pourcentage d'application (-1 = application forc\u00e9e)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("id de l'\u00e9tat qui va modifier la proba", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("appliquer si \u00e9tat pas pr\u00e9sent (0=non, 1=oui)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("modification du % par niveau de l'\u00e9tat", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Verifier l'etat modificateur sur la cible", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("pourcentage d'application (-1 = application forc\u00e9e)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("id de l'\u00e9tat qui va modifier la proba", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("appliquer si \u00e9tat pas pr\u00e9sent (0=non, 1=oui)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("modification du % par niveau de l'\u00e9tat", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("etat modificateur sur la cible (0=non (defaut), 1=oui)", WakfuRunningEffectParameterType.CONFIG) }) });
        (UNDECURSABLE_STATE = new TIntArrayList()).add(PrimitiveArrays.EMPTY_INT_ARRAY);
    }
}
