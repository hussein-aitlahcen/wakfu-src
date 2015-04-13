package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar;

public interface GameCalendarEventListener
{
    void onCalendarEvent(CalendarEventType p0, GameCalendar p1);
    
    public enum CalendarEventType
    {
        CALENDAR_UPDATED, 
        CALENDAR_SYNCHRONIZED, 
        EVENT_ADDED, 
        EVENT_REMOVED, 
        EVENT_RUNNED;
    }
}
