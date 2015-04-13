package com.ankamagames.wakfu.common.game.lock;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public interface LockContextHandler
{
    void onLock(int p0, GameDateConst p1);
    
    void onLockIncrement(int p0, int p1, GameDateConst p2);
}
