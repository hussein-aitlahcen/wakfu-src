package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar;

import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.event.*;

public class CalendarEventManager
{
    protected final SortedList<CalendarEvent> m_events;
    
    public CalendarEventManager() {
        super();
        this.m_events = new SortedList<CalendarEvent>(CalendarEvent.COMPARATOR);
    }
    
    public void addEvent(final CalendarEvent event) {
        this.m_events.add(event);
    }
    
    public CalendarEvent getFirstEvent() {
        return this.m_events.getFirst();
    }
    
    public void removeEvent(final CalendarEvent event) {
        this.m_events.remove(event);
    }
    
    public SortedList<CalendarEvent> getEvents() {
        return this.m_events;
    }
}
