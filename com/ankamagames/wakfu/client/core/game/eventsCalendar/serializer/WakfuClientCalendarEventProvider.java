package com.ankamagames.wakfu.client.core.game.eventsCalendar.serializer;

import com.ankamagames.wakfu.common.game.eventsCalendar.serialisation.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;

public class WakfuClientCalendarEventProvider implements WakfuCalendarEventProvider
{
    private static WakfuClientCalendarEventProvider m_instance;
    
    public static WakfuClientCalendarEventProvider getInstance() {
        return WakfuClientCalendarEventProvider.m_instance;
    }
    
    @Override
    public WakfuCalendarEvent getNewEventInstance() {
        return new WakfuCalendarEvent();
    }
    
    @Override
    public void freeEventInstance(final WakfuCalendarEvent event) {
    }
    
    static {
        WakfuClientCalendarEventProvider.m_instance = new WakfuClientCalendarEventProvider();
    }
}
