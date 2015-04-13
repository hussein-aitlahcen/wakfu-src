package com.ankamagames.wakfu.client.ui.protocol.message.eventsCalendar;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.eventsCalendar.*;

public class UIEventsCalendarMessage extends UIMessage
{
    private WakfuCalendarEventFieldProvider m_event;
    
    public WakfuCalendarEventFieldProvider getEvent() {
        return this.m_event;
    }
    
    public void setEvent(final WakfuCalendarEventFieldProvider event) {
        this.m_event = event;
    }
}
