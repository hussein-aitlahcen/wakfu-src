package com.ankamagames.wakfu.common.game.eventsCalendar;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.*;

public interface EventsCalendar<CE extends CalendarEvent>
{
    void addCalendarEvent(CE p0);
    
    Set<CE> getEventsByDate(GameDateConst p0);
    
    void removeCalendarEvent(WakfuCalendarEvent p0);
    
    Set<CE> getEvents();
    
    void clear();
    
    boolean isEmpty();
    
    boolean asIdenticalEvent(WakfuCalendarEvent p0);
    
    int size();
}
