package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;

public interface WakfuFightEffect extends WakfuEffect
{
    byte getEffectType();
    
    RelativeFightTimeInterval getDuration(short p0);
    
    RelativeFightTimeInterval getDelay(short p0);
    
    boolean isDecursable();
}
