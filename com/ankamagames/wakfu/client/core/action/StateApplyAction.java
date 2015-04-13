package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.effect.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;

public class StateApplyAction extends AbstractFightAction
{
    private final WakfuRunningEffect m_runningEffect;
    private final byte[] m_serializedRunningEffect;
    
    public StateApplyAction(final int uniqueId, final int actionType, final int fightId, final int actionId, final byte[] serializedRunningEffect) {
        super(uniqueId, actionType, actionId, fightId);
        final StaticRunningEffect<WakfuEffect, WakfuEffectContainer> staticEffect = ((Constants<StaticRunningEffect<WakfuEffect, WakfuEffectContainer>>)RunningEffectConstants.getInstance()).getObjectFromId(RunningEffectConstants.RUNNING_STATE.getId());
        this.m_runningEffect = (WakfuRunningEffect)staticEffect.newInstance(WakfuInstanceEffectContext.getInstance(), EffectManager.getInstance());
        this.m_serializedRunningEffect = serializedRunningEffect;
    }
    
    @Override
    protected void runCore() {
        this.m_runningEffect.fromBuild(this.m_serializedRunningEffect);
        final EffectUser target = this.m_runningEffect.getTarget();
        if (target == null) {
            return;
        }
        if (!this.m_runningEffect.hasDuration()) {
            return;
        }
        if (target.getRunningEffectManager() != null) {
            target.getRunningEffectManager().storeEffect(this.m_runningEffect);
        }
        this.m_runningEffect.onApplication();
        if (!this.m_runningEffect.mustBeTriggered()) {
            this.m_runningEffect.askForExecution();
        }
    }
    
    @Override
    protected void onActionFinished() {
        this.m_runningEffect.releaseIfNeed();
    }
}
