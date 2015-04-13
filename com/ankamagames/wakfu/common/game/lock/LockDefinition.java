package com.ankamagames.wakfu.common.game.lock;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public interface LockDefinition
{
    public static final int INVALID_PERIOD = -1;
    
    int getId();
    
    int getLockedItem();
    
    int getLockValue();
    
    boolean isAvailableToCitizensOnly();
    
    GameIntervalConst getPeriodDuration();
    
    GameDateConst getPeriodStartTime();
    
    GameDateConst getUnlockDate();
}
