package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.wakfu.common.game.effect.runningEffect.*;

public interface WakfuRunningEffectListener
{
    void onAfterExecution(WakfuRunningEffect p0);
    
    void valueComputed(WakfuRunningEffect p0);
}
