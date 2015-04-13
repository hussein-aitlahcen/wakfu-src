package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.eventsCalendar.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.serialisation.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;

public class EventsCalendarInfosAnswerMessage extends InputOnlyProxyMessage
{
    private EventsCalendar m_calendar;
    private byte[] m_rawCalendar;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        this.m_rawCalendar = rawDatas;
        return true;
    }
    
    public EventsCalendar getCalendar() {
        if (this.m_calendar == null) {
            final WakfuEventsCalendarSerializer serializable = new WakfuEventsCalendarSerializer(new CharacterEventsCalendar());
            serializable.fromBuild(this.m_rawCalendar);
            this.m_calendar = serializable.getEventsCalendar();
        }
        return this.m_calendar;
    }
    
    @Override
    public int getId() {
        return 562;
    }
}
