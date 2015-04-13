package com.ankamagames.wakfu.common.game.guild;

import com.ankamagames.wakfu.common.game.time.calendar.*;

final class GuildPointsHandler
{
    private int m_currentGuildPoints;
    private int m_totalGuildPoints;
    private int m_earnedPointsWeekly;
    private int m_weeklyPointsLimit;
    private int m_lastEarningPointWeek;
    
    GuildPointsHandler() {
        super();
        this.m_weeklyPointsLimit = 7500;
        final WakfuGameCalendar calendar = WakfuGameCalendar.getInstance();
        this.m_lastEarningPointWeek = calendar.get(3);
    }
    
    int getCurrentGuildPoints() {
        return this.m_currentGuildPoints;
    }
    
    void setCurrentGuildPoints(final int currentGuildPoints) {
        this.m_currentGuildPoints = currentGuildPoints;
    }
    
    int getTotalGuildPoints() {
        return this.m_totalGuildPoints;
    }
    
    void setTotalGuildPoints(final int totalGuildPoints) {
        this.m_totalGuildPoints = totalGuildPoints;
    }
    
    int getEarnedPointsWeekly() {
        return this.m_earnedPointsWeekly;
    }
    
    void setEarnedPointsWeekly(final int earnedPointsWeekly) {
        this.m_earnedPointsWeekly = earnedPointsWeekly;
    }
    
    int getWeeklyPointsLimit() {
        return this.m_weeklyPointsLimit;
    }
    
    void setWeeklyPointsLimit(final int weeklyPointsLimit) {
        this.m_weeklyPointsLimit = weeklyPointsLimit;
    }
    
    int getLastEarningPointWeek() {
        return this.m_lastEarningPointWeek;
    }
    
    void setLastEarningPointWeek(final int lastEarningPointWeek) {
        this.m_lastEarningPointWeek = lastEarningPointWeek;
    }
}
