package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class RunningEffectActivationEvent extends RunningEffectEvent
{
    protected RunningEffectActivationEvent() {
        super();
    }
    
    public RunningEffectActivationEvent(final RunningEffect re, final long targetId) {
        this();
        this.setRunningEffect(re);
        this.setTargetId(targetId);
    }
    
    @Override
    protected void safelySendTo(final TimeEventHandler handler) {
        handler.handleRunningEffectActivationEvent(this);
    }
    
    @Override
    public void clean() {
        if (this.m_runningEffect != null) {
            this.m_runningEffect.release();
        }
        this.setRunningEffectToNull();
    }
}
