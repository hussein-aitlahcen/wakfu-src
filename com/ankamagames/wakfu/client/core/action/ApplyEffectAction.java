package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class ApplyEffectAction extends AbstractFightScriptedAction
{
    private WakfuRunningEffect m_runningEffect;
    private final byte[] m_serializedRunningEffect;
    
    public ApplyEffectAction(final int uniqueId, final int actionType, final int fightId, final int actionId, final StaticRunningEffect<WakfuEffect, WakfuEffectContainer> staticEffect, final byte[] serializedRunningEffect) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_runningEffect = null;
        final FightInfo fight = this.getFight();
        this.m_runningEffect = (WakfuRunningEffect)staticEffect.newInstance((fight != null) ? fight.getContext() : null, EffectManager.getInstance());
        this.m_serializedRunningEffect = serializedRunningEffect;
    }
    
    @Override
    public long onRun() {
        if (this.m_runningEffect != null) {
            if (this.m_runningEffect.getContext() == null) {
                final FightInfo fight = this.getFight();
                if (fight != null) {
                    this.m_runningEffect.setContext(fight.getContext());
                }
                else {
                    this.m_runningEffect.setContext(WakfuInstanceEffectContext.getInstance());
                }
            }
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
        EffectUser fighterToStoreEffect;
        if (this.m_runningEffect.useTargetCell() || this.m_runningEffect.mustStoreOnCaster()) {
            fighterToStoreEffect = this.m_runningEffect.getCaster();
        }
        else {
            fighterToStoreEffect = this.m_runningEffect.getTarget();
        }
        if ((this.m_runningEffect.hasDuration() || this.m_runningEffect.hasExecutionDelay()) && fighterToStoreEffect != null && (this.m_runningEffect.mustBeTriggered() || this.m_runningEffect.hasExecutionDelay()) && fighterToStoreEffect.getRunningEffectManager() != null) {
            fighterToStoreEffect.getRunningEffectManager().storeEffect(this.m_runningEffect);
        }
        this.m_runningEffect.onApplication();
        this.m_runningEffect.releaseIfNeed();
    }
    
    public int getEffectValue() {
        return this.m_runningEffect.getValue();
    }
}
