package com.ankamagames.wakfu.common.game.lock;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class LockConstants
{
    public static final int DAILY_GENERAL_LOCK_ID = -1;
    public static final int DAILY_GENERAL_TRIES = 3;
    private static final GameDateConst DAILY_GENERAL_PERIOD_START_DATE;
    public static final Lock DAILY_LOCK_DEFINITION;
    
    static {
        DAILY_GENERAL_PERIOD_START_DATE = new GameDate(0, 0, 0, 1, 12, 2013);
        DAILY_LOCK_DEFINITION = new Lock(-1, -1, 3, LockConstants.DAILY_GENERAL_PERIOD_START_DATE, GameIntervalConst.DAY_INTERVAL, LockConstants.DAILY_GENERAL_PERIOD_START_DATE, false);
    }
}
