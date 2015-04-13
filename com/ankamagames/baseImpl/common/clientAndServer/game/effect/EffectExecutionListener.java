package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public interface EffectExecutionListener
{
    void onEffectDirectExecution(RunningEffect p0);
    
    void onEffectTriggeredExecution(RunningEffect p0);
    
    void onEffectApplication(RunningEffect p0);
    
    void onEffectUnApplication(RunningEffect p0);
}
