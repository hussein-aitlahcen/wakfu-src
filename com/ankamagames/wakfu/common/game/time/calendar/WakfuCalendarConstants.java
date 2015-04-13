package com.ankamagames.wakfu.common.game.time.calendar;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class WakfuCalendarConstants
{
    public static final int WAKFU_COEF_SPEED_TIME = 1;
    public static final int REFERENCE_GAME_YEAR = 1970;
    public static final int UPDATE_FREQUENCY = 1000;
    public static final int WAKFU_CYCLE_DURATION = 86400;
    public static final GameIntervalConst WAKFU_DAY_CYCLE_DURATION;
    public static final GameIntervalConst WAKFU_DAY_CYCLE_UPDATE_FREQUENCY;
    public static final boolean ALLOW_DAYLIGHT = true;
    public static final int NIGHT_START_PERCENTAGE = 83;
    public static final int NIGHT_END_PERCENTAGE = 17;
    
    static {
        WAKFU_DAY_CYCLE_DURATION = new GameInterval(0, 0, 4, 0);
        WAKFU_DAY_CYCLE_UPDATE_FREQUENCY = new GameInterval(30, 12, 0, 0);
    }
}
