package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public abstract class CyclicCalendarEvent extends CalendarEvent
{
    protected GameDate m_endDate;
    protected EventPeriod m_period;
    
    protected CyclicCalendarEvent(final GameDateConst startDate, final GameDateConst endDate, final EventPeriod period) {
        super(startDate);
        this.m_endDate = new GameDate(endDate);
        this.m_period = new EventPeriod(period);
    }
    
    public GameDateConst getEndDate() {
        return this.m_endDate;
    }
    
    public EventPeriod getPeriodicity() {
        return this.m_period;
    }
}
