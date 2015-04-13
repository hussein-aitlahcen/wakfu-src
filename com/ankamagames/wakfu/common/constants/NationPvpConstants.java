package com.ankamagames.wakfu.common.constants;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.concurrent.*;

public class NationPvpConstants
{
    public static final GameDateConst PVP_START_DATE;
    public static final short PVP_MIN_LEVEL = 38;
    public static final GameIntervalConst PVP_CALENDAR_INTERVAL;
    public static final GameIntervalConst PVP_CACHE_CALENDAR_INTERVAL;
    public static final EventPeriod PVP_CALENDAR_EVENT_PERIOD;
    public static final EventPeriod PVP_CACHE_CALENDAR_EVENT_PERIOD;
    public static final short MAX_PLAYER_LEVEL_DIFF_VALIDITY = 50;
    public static final short MAX_PLAYER_RANK_DIFF = 4;
    public static final short LEVEL_DIFF_TO_DOUBLE_PLAYER_POWER = 30;
    public static final short RANK_DIFF_TO_DOUBLE_PLAYER_POWER = 7;
    public static final short CLASSIC_RANK_COUNT = 7;
    public static final int BASE_REWARD_POINT = 100;
    public static final double MAX_DIFF_TEAM_VALUES = 1.8;
    public static final double MAX_DIFF_PLAYER_VALUES = 1.8;
    public static final int WINNING_TEAM_POWER = 2;
    public static final int PVP_DISACTIVATION_MALUS = 100;
    public static final int LOSING_TEAM_POWER = 2;
    public static final int WINNING_PLAYER_POWER = 2;
    public static final int MAX_NUM_VALID_CONSECUTIVE_IDENTICAL_OPPONENT = 4;
    public static final int NUM_CONSECUTIVE_OPPONENTS_CACHE_SIZE = 30;
    public static final double WEEKLY_SCORE_MALUS_RATIO = 0.10000000149011612;
    public static final GameIntervalConst PVP_START_DURATION;
    public static final GameIntervalConst PVP_LEAVE_DURATION;
    public static final long INTERACTION_WAITING_DURATION;
    public static final long DAILY_MAX_PVP_MONEY = 1000L;
    
    static {
        PVP_START_DATE = new GameDate(0, 0, 0, 4, 8, 2014);
        PVP_CALENDAR_INTERVAL = new GameInterval(0, 0, 0, 7);
        PVP_CACHE_CALENDAR_INTERVAL = new GameInterval(0, 1, 0, 0);
        PVP_CALENDAR_EVENT_PERIOD = new EventPeriod(NationPvpConstants.PVP_CALENDAR_INTERVAL);
        PVP_CACHE_CALENDAR_EVENT_PERIOD = new EventPeriod(NationPvpConstants.PVP_CACHE_CALENDAR_INTERVAL);
        PVP_START_DURATION = new GameInterval(0, 5, 0, 0);
        PVP_LEAVE_DURATION = new GameInterval(0, 0, 4, 0);
        INTERACTION_WAITING_DURATION = TimeUnit.SECONDS.toMillis(6L);
    }
}
