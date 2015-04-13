package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.serialisation.*;
import com.ankamagames.wakfu.common.constants.*;

public class CalendarEventNotificationMessage extends InputOnlyProxyMessage
{
    private Set<WakfuCalendarEvent> m_calendarEvents;
    
    public CalendarEventNotificationMessage() {
        super();
        this.m_calendarEvents = new HashSet<WakfuCalendarEvent>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte nbEvents = bb.get();
        final WakfuCalendarEventSerializer serializable = new WakfuCalendarEventSerializer();
        for (int i = 0; i < nbEvents; ++i) {
            serializable.TITLE.unserialize(bb, Version.SERIALIZATION_VERSION);
            serializable.START_DATE.unserialize(bb, Version.SERIALIZATION_VERSION);
            this.m_calendarEvents.add(serializable.getEvent());
            serializable.setEventForSerialisation(null);
        }
        return true;
    }
    
    public Set<WakfuCalendarEvent> getCalendarEvents() {
        return this.m_calendarEvents;
    }
    
    @Override
    public int getId() {
        return 568;
    }
}
