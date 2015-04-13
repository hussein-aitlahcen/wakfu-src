package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public final class NullEffectExecutionListener implements EffectExecutionListener
{
    @Override
    public void onEffectDirectExecution(final RunningEffect effect) {
    }
    
    @Override
    public void onEffectTriggeredExecution(final RunningEffect effect) {
    }
    
    @Override
    public void onEffectApplication(final RunningEffect effect) {
    }
    
    @Override
    public void onEffectUnApplication(final RunningEffect effect) {
    }
}
