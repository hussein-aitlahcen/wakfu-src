package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public abstract class CalendarEvent
{
    private static final Logger m_logger;
    public static Comparator<CalendarEvent> COMPARATOR;
    protected GameDate m_date;
    
    protected CalendarEvent(final GameDateConst date) {
        super();
        this.m_date = new GameDate(date);
    }
    
    public GameDateConst getDate() {
        return this.m_date;
    }
    
    public abstract void runEvent(final GameCalendar p0);
    
    public CalendarEvent addToDate(final int s, final int min, final int h, final int d) {
        this.m_date.add(s, min, h, d);
        return this;
    }
    
    public CalendarEvent addToDate(final EventPeriod period) {
        this.m_date.add(period.getSeconds(), period.getMinutes(), period.getHours(), period.getDays(), period.getMonths(), period.getYears());
        return this;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CalendarEvent.class);
        CalendarEvent.COMPARATOR = new Comparator<CalendarEvent>() {
            @Override
            public int compare(final CalendarEvent o1, final CalendarEvent o2) {
                return o1.m_date.compareTo((GameDateConst)o2.m_date);
            }
        };
    }
}
