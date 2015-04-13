package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class EventsCalendarInfosRequestMessage extends OutputOnlyProxyMessage
{
    private byte m_calendarType;
    
    public void setCalendarType(final byte calendarType) {
        this.m_calendarType = calendarType;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(1);
        bb.put(this.m_calendarType);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 561;
    }
}
