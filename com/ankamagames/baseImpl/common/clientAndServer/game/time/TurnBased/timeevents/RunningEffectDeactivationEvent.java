package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class RunningEffectDeactivationEvent extends RunningEffectEvent
{
    protected RunningEffectDeactivationEvent() {
        super();
    }
    
    public RunningEffectDeactivationEvent(final RunningEffect re, final long targetId) {
        super();
        this.initialize(re, targetId);
    }
    
    public static RunningEffectDeactivationEvent checkOut(final RunningEffect re, final long targetId) {
        return new RunningEffectDeactivationEvent(re, targetId);
    }
    
    @Override
    protected void safelySendTo(final TimeEventHandler handler) {
        handler.handleRunningEffectDeactivationEvent(this);
    }
}
