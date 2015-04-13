package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class UnApplyEffectAction extends AbstractFightScriptedAction
{
    private final byte[] m_serializedRunningEffect;
    private final long m_targetId;
    private final long m_ruid;
    private WakfuRunningEffect m_runningEffect;
    
    public UnApplyEffectAction(final int uniqueId, final int actionType, final int fightId, final int actionId, final StaticRunningEffect<WakfuEffect, WakfuEffectContainer> staticEffect, final long reUID, final long targetid, final byte[] serializedRunningEffect) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_serializedRunningEffect = serializedRunningEffect;
        this.m_targetId = targetid;
        this.m_ruid = reUID;
    }
    
    @Override
    public long onRun() {
        final CharacterInfo target = this.getFighterById(this.m_targetId);
        if (target != null) {
            if (target.getRunningEffectManager() != null) {
                this.m_runningEffect = (WakfuRunningEffect)target.getRunningEffectManager().getRunningEffectFromUID(this.m_ruid);
            }
        }
        if (this.m_runningEffect != null) {
            this.m_runningEffect.fromBuild(this.m_serializedRunningEffect);
            if (this.m_runningEffect.getTarget() != null) {
                this.setTargetId(this.m_runningEffect.getTarget().getId());
            }
            if (this.m_runningEffect.getCaster() != null) {
                this.setInstigatorId(this.m_runningEffect.getCaster().getId());
            }
            this.m_runningEffect.disableValueComputation();
        }
        return super.onRun();
    }
    
    @Override
    protected void onActionFinished() {
        if (this.m_runningEffect == null) {
            return;
        }
        if (!this.m_runningEffect.unapplicationMustBeNotified()) {
            return;
        }
        if (this.m_runningEffect.getManagerWhereIamStored() != null) {
            this.m_runningEffect.getManagerWhereIamStored().removeEffect(this.m_runningEffect);
        }
        else {
            this.m_runningEffect.releaseIfNeed();
        }
    }
    
    public int getEffectValue() {
        return this.m_runningEffect.getValue();
    }
}
