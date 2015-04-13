package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class SeasonEvent extends CyclicCalendarEvent
{
    private final Season m_season;
    
    public SeasonEvent(final GameDateConst date, final Season season, final EventPeriod period) {
        super(date, null, period);
        this.m_season = season;
    }
    
    @Override
    public void runEvent(final GameCalendar calendar) {
        calendar.setSeason(this.m_season);
    }
    
    public Season getSeason() {
        return this.m_season;
    }
}
