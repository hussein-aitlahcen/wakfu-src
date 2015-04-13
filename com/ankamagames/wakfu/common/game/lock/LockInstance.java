package com.ankamagames.wakfu.common.game.lock;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public interface LockInstance extends LockDefinition
{
    GameDateConst getLockDate();
    
    GameDateConst getCurrentLockValueLastChange();
    
    int getCurrentLockValue();
}
