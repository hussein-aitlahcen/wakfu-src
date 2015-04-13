package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RemoveCalendarEventRequestMessage extends OutputOnlyProxyMessage
{
    long m_eventUid;
    
    public void setEventUid(final long eventUid) {
        this.m_eventUid = eventUid;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(this.m_eventUid);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 565;
    }
}
