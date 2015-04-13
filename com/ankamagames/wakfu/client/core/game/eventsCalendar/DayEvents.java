package com.ankamagames.wakfu.client.core.game.eventsCalendar;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import java.util.*;

public class DayEvents implements DateComponent
{
    private final int m_dayInMonth;
    private final ArrayList<WakfuCalendarEventFieldProvider> m_events;
    
    public DayEvents(final int dayInMonth, final Set<WakfuCalendarEvent> events) {
        super();
        this.m_dayInMonth = dayInMonth;
        this.m_events = new ArrayList<WakfuCalendarEventFieldProvider>(events.size());
        final Iterator<WakfuCalendarEvent> it = events.iterator();
        while (it.hasNext()) {
            final WakfuCalendarEventFieldProvider fp = new WakfuCalendarEventFieldProvider();
            fp.setEvent(it.next());
            this.m_events.add(fp);
        }
        Collections.sort(this.m_events);
    }
    
    @Override
    public int getDayInMonth() {
        return this.m_dayInMonth;
    }
    
    @Override
    public Object getContent() {
        return this.m_events;
    }
}
