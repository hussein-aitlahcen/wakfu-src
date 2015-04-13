package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar;

import org.jetbrains.annotations.*;

public interface GameIntervalConst
{
    public static final GameIntervalConst YEAR_INTERVAL = new GameInterval(0, 0, 6, 365);
    public static final GameIntervalConst WEEK_INTERVAL = new GameInterval(0, 0, 0, 7);
    public static final GameIntervalConst DAY_INTERVAL = new GameInterval(0, 0, 0, 1);
    public static final GameIntervalConst HOUR_INTERVAL = new GameInterval(0, 0, 1, 0);
    public static final GameIntervalConst MINUTE_INTERVAL = new GameInterval(0, 1, 0, 0);
    public static final GameIntervalConst SECOND_INTERVAL = new GameInterval(1, 0, 0, 0);
    public static final GameIntervalConst EMPTY_INTERVAL = new GameInterval(0, 0, 0, 0);
    
    int getSeconds();
    
    int getMinutes();
    
    int getHours();
    
    int getDays();
    
    int getDivisionResult(GameIntervalConst p0);
    
    boolean isPositive();
    
    boolean isEmpty();
    
    long toSeconds();
    
    long toLong();
    
    boolean greaterThan(@NotNull GameIntervalConst p0);
    
    boolean lowerThan(@NotNull GameIntervalConst p0);
}
