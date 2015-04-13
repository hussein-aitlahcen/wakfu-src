package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar;

import org.jetbrains.annotations.*;
import java.util.*;

public interface GameDateConst extends Comparable<GameDateConst>
{
    String toString();
    
    boolean isNull();
    
    String toDescriptionString();
    
    boolean before(@NotNull GameDateConst p0);
    
    boolean beforeOrEquals(@NotNull GameDateConst p0);
    
    boolean after(@NotNull GameDateConst p0);
    
    boolean afterOrEquals(@NotNull GameDateConst p0);
    
    int compareTo(GameDateConst p0);
    
    GameInterval timeTo(GameDateConst p0);
    
    GameDateConst closestDatePeriod(GameDateConst p0, GameIntervalConst p1);
    
    GameDateConst closestDatePeriod(GameDateConst p0, GameIntervalConst p1, boolean p2);
    
    long toLong();
    
    Date toJavaDate();
    
    long hourToLong();
    
    long dayToLong();
    
    int getDay();
    
    int getHours();
    
    int getMinutes();
    
    int getMonth();
    
    int getSeconds();
    
    int getYear();
    
    void normalize();
    
    GameDateConst daysAgo(int p0);
    
    GameDateConst daysLater(int p0);
    
    GameDateConst yesterday();
    
    GameDateConst tomorrow();
}
