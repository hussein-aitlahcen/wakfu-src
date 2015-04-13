package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.serialisation.*;

public class CalendarEventsUpdateMessage extends InputOnlyProxyMessage
{
    private Set<WakfuCalendarEvent> m_calendarEvents;
    
    public CalendarEventsUpdateMessage() {
        super();
        this.m_calendarEvents = new HashSet<WakfuCalendarEvent>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte nbEvents = bb.get();
        final WakfuCalendarEventSerializer serializable = new WakfuCalendarEventSerializer();
        for (int i = 0; i < nbEvents; ++i) {
            serializable.unserialize(bb);
            this.m_calendarEvents.add(serializable.getEvent());
        }
        return true;
    }
    
    public Set<WakfuCalendarEvent> getCalendarEvents() {
        return this.m_calendarEvents;
    }
    
    @Override
    public int getId() {
        return 578;
    }
}
