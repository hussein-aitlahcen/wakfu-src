package com.ankamagames.wakfu.common.game.eventsCalendar.serialisation;

import com.ankamagames.wakfu.common.game.eventsCalendar.*;

public interface WakfuCalendarEventProvider
{
    WakfuCalendarEvent getNewEventInstance();
    
    void freeEventInstance(WakfuCalendarEvent p0);
}
