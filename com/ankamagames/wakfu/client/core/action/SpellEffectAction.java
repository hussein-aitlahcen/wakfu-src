package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect.*;
import com.ankamagames.wakfu.client.core.effect.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class SpellEffectAction extends AbstractFightScriptedAction implements SpellEffectActionInterface
{
    private final SpellEffectChatLogger m_spellEffectChatLogger;
    private final WakfuRunningEffect m_runningEffect;
    private final byte[] m_serializedRunningEffect;
    private final byte[] m_serializedTarget;
    private final boolean m_isTriggered;
    private boolean m_initialized;
    private int m_value;
    private int m_armorLoss;
    private int m_barrierLoss;
    
    public SpellEffectAction(final int uniqueId, final int actionType, final int fightId, final int actionId, final StaticRunningEffect<WakfuEffect, WakfuEffectContainer> staticEffect, final byte[] serializedRunningEffectWithoutTargets, final boolean isTriggered, final byte[] serializedTarget) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_spellEffectChatLogger = new SpellEffectChatLogger(this);
        this.m_runningEffect = (WakfuRunningEffect)staticEffect.newInstance(null, EffectManager.getInstance());
        this.m_isTriggered = isTriggered;
        this.m_serializedRunningEffect = serializedRunningEffectWithoutTargets;
        this.m_serializedTarget = serializedTarget;
        this.addJavaFunctionsLibrary(new SpellEffectFunctionsLibrary(this));
        this.setScriptFileId(getScriptIdFromEffectId(staticEffect.getId()));
    }
    
    public SpellEffectAction(final int uniqueId, final int type, final int actionId, final int fightInfoId, final WakfuRunningEffect runningEffect) {
        super(uniqueId, type, actionId, fightInfoId);
        this.m_spellEffectChatLogger = new SpellEffectChatLogger(this);
        this.m_runningEffect = runningEffect;
        this.m_isTriggered = false;
        this.m_serializedRunningEffect = null;
        this.m_serializedTarget = null;
        this.addJavaFunctionsLibrary(new SpellEffectFunctionsLibrary(this));
        this.setScriptFileId(getScriptIdFromEffectId(this.m_runningEffect.getEffectId()));
    }
    
    public void initEffectCore() {
        final FightInfo fightInfo = this.getFight();
        if (this.m_runningEffect != null) {
            if (fightInfo != null) {
                this.m_runningEffect.setContext(fightInfo.getContext());
            }
            else {
                this.m_runningEffect.setContext(WakfuInstanceEffectContext.getInstance());
            }
        }
        this.m_runningEffect.extractPartFromBuild(this.m_runningEffect.getCoreBinarSerialPart(), this.m_serializedRunningEffect, Version.SERIALIZATION_VERSION);
    }
    
    public void initAction() {
        this.m_initialized = true;
        if (this.m_runningEffect == null) {
            return;
        }
        final FightInfo fightInfo = this.getFight();
        if (fightInfo != null) {
            this.m_runningEffect.setContext(fightInfo.getContext());
        }
        else {
            this.m_runningEffect.setContext(WakfuInstanceEffectContext.getInstance());
        }
        if (this.m_serializedTarget != null) {
            this.m_runningEffect.getTargetBinarSerialPart().unserialize(ByteBuffer.wrap(this.m_serializedTarget), Version.SERIALIZATION_VERSION);
        }
        this.m_runningEffect.fromBuild(this.m_serializedRunningEffect);
        if (this.m_runningEffect.getTarget() != null) {
            this.setTargetId(this.m_runningEffect.getTarget().getId());
        }
        if (this.m_runningEffect.getCaster() != null) {
            this.setInstigatorId(this.m_runningEffect.getCaster().getId());
        }
        if (((RunningEffect<FX, WakfuEffectContainer>)this.m_runningEffect).getEffectContainer() != null && ((RunningEffect<FX, WakfuEffectContainer>)this.m_runningEffect).getEffectContainer().getContainerType() == 17) {
            this.setScriptFileId(-1);
        }
        final int scriptFileId = this.getGenericEffectScriptFileId();
        if (scriptFileId != 0) {
            this.setScriptFileId(scriptFileId);
        }
        this.m_value = this.m_runningEffect.getValue();
        if (this.m_runningEffect instanceof ArmorLossProvider) {
            this.m_armorLoss = ((ArmorLossProvider)this.m_runningEffect).getArmorLoss();
            this.m_barrierLoss = ((ArmorLossProvider)this.m_runningEffect).getBarrierLoss();
        }
        else {
            this.m_armorLoss = 0;
            this.m_barrierLoss = 0;
        }
    }
    
    public void setInitialized(final boolean initialized) {
        this.m_initialized = initialized;
    }
    
    @Override
    public long getTargetId() {
        if (this.m_initialized) {
            return super.getTargetId();
        }
        if (super.getTargetId() == Long.MIN_VALUE && this.m_runningEffect != null) {
            if (this.m_serializedTarget != null) {
                this.m_runningEffect.getTargetBinarSerialPart().unserialize(ByteBuffer.wrap(this.m_serializedTarget), Version.SERIALIZATION_VERSION);
            }
            this.m_runningEffect.fromBuild(this.m_serializedRunningEffect);
            if (this.m_runningEffect.getTarget() != null) {
                this.setTargetId(this.m_runningEffect.getTarget().getId());
            }
        }
        return super.getTargetId();
    }
    
    @Override
    public long getCasterId() {
        return this.getInstigatorId();
    }
    
    @Override
    public long onRun() {
        this.initAction();
        return super.onRun();
    }
    
    public long runWithoutInit() {
        return super.onRun();
    }
    
    private int getGenericEffectScriptFileId() {
        if (((RunningEffect<WakfuEffect, EC>)this.m_runningEffect).getGenericEffect() == null) {
            return 0;
        }
        return ((RunningEffect<WakfuEffect, EC>)this.m_runningEffect).getGenericEffect().getScriptFileId();
    }
    
    public void onActionFinished() {
        try {
            this.m_spellEffectChatLogger.displayChatMessage();
        }
        catch (Exception e) {
            SpellEffectAction.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        this.executeEffect();
        super.onActionFinished();
        this.m_runningEffect.releaseIfNeed();
    }
    
    private void executeEffect() {
        this.m_runningEffect.disableValueComputation();
        RunningEffect.resetLimitedApplyCount();
        this.m_runningEffect.forceDontTriggerAnything();
        if (this.m_isTriggered) {
            this.m_runningEffect.forceInstant();
            this.m_runningEffect.execute(null, true);
        }
        else {
            if (this.m_runningEffect.hasDuration()) {
                boolean effectStored = false;
                final EffectUser caster = this.m_runningEffect.getCaster();
                final boolean effectOnCellOnly = this.m_runningEffect.useTargetCell() && !this.m_runningEffect.useTarget();
                final boolean effectOnCaster = this.m_runningEffect.mustStoreOnCaster();
                if (effectOnCaster || effectOnCellOnly) {
                    if (caster != null && caster.getRunningEffectManager() != null) {
                        caster.getRunningEffectManager().storeEffect(this.m_runningEffect);
                        effectStored = true;
                    }
                }
                else if (this.m_runningEffect.getTarget() != null && this.m_runningEffect.getTarget().getRunningEffectManager() != null) {
                    this.m_runningEffect.getTarget().getRunningEffectManager().storeEffect(this.m_runningEffect);
                    effectStored = true;
                }
                this.m_runningEffect.onApplication();
                if (!effectStored) {
                    if (((RunningEffect<WakfuEffect, EC>)this.m_runningEffect).getGenericEffect() != null) {
                        SpellEffectAction.m_logger.error((Object)("Unable to find a valid RunningEffectManager to apply effect d'id " + ((RunningEffect<WakfuEffect, EC>)this.m_runningEffect).getGenericEffect().getEffectId()));
                    }
                    else {
                        SpellEffectAction.m_logger.error((Object)("Unable to find a valid RunningEffectManager to apply effect " + this.m_runningEffect));
                    }
                }
            }
            if (this.m_runningEffect.hasDuration() && !this.m_runningEffect.isInfinite()) {
                this.m_runningEffect.pushRunningEffectDurationTimeEventInTimelineAfterDelay();
            }
            if (!this.m_runningEffect.mustBeTriggered()) {
                this.m_runningEffect.execute(this.m_runningEffect.getParent(), false);
            }
        }
    }
    
    public void displayChatMessage(final String msg) {
        if (msg != null && msg.length() > 0) {
            SpellEffectAction.m_fightLogger.info(msg);
        }
    }
    
    private static int getScriptIdFromEffectId(final int effectId) {
        final RunningEffectDefinition definition = (RunningEffectDefinition)RunningEffectConstants.getInstance().getConstantDefinition(effectId);
        if (definition == null) {
            return -1;
        }
        return definition.getScriptId();
    }
    
    @Override
    public int getEffectValue() {
        return this.m_value;
    }
    
    @Override
    public byte getExecutionStatus() {
        return this.m_runningEffect.getExecutionStatus();
    }
    
    @Override
    public WakfuRunningEffect getRunningEffect() {
        return this.m_runningEffect;
    }
    
    @Override
    public Point3 getPosition() {
        final EffectUser target = this.m_runningEffect.getTarget();
        if (target != null) {
            return new Point3(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude());
        }
        return this.m_runningEffect.getTargetCell();
    }
    
    @Override
    public Point3 getTargetCell() {
        final Point3 targetCell = this.m_runningEffect.getTargetCell();
        if (targetCell != null) {
            return targetCell;
        }
        final EffectUser target = this.m_runningEffect.getTarget();
        if (target != null) {
            return new Point3(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude());
        }
        return null;
    }
    
    @Override
    public Point3 getCasterPosition() {
        final EffectUser caster = this.m_runningEffect.getCaster();
        if (caster != null) {
            return new Point3(caster.getWorldCellX(), caster.getWorldCellY(), caster.getWorldCellAltitude());
        }
        SpellEffectAction.m_logger.error((Object)"ATTENTION : on veut r\u00e9cup\u00e9rer la position du caster d'un effet mais celui ci est inconnu, \u00e0 d\u00e9faut on renvoie la cellule cible");
        return this.m_runningEffect.getTargetCell();
    }
    
    @Override
    public Point3 getBearerPosition() {
        final RunningEffectManager manager = this.m_runningEffect.getManagerWhereIamStored();
        if (manager == null) {
            return null;
        }
        final EffectUser owner = manager.getOwner();
        if (owner == null) {
            return null;
        }
        return owner.getPosition();
    }
    
    @Override
    public int getArmorLossValue() {
        return this.m_armorLoss;
    }
    
    @Override
    public int getBarrierLossValue() {
        return this.m_barrierLoss;
    }
    
    @Override
    public int getSpecialId() {
        if (this.m_runningEffect != null && ((RunningEffect<WakfuEffect, EC>)this.m_runningEffect).getGenericEffect() != null) {
            return ((RunningEffect<WakfuEffect, EC>)this.m_runningEffect).getGenericEffect().getEffectId();
        }
        return super.getSpecialId();
    }
}
