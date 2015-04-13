package com.ankamagames.baseImpl.common.clientAndServer.game.time;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public interface BasicTimeline
{
    TimeEventHandler getGlobalListener();
    
    void start();
    
    void stop();
    
    boolean isRunning();
    
    void fromBuild(EffectContext p0, byte[] p1);
    
    byte[] serializeTimeline();
    
    int getFightId();
    
    void setFightId(int p0);
}
