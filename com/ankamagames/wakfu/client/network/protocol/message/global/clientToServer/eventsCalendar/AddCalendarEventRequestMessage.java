package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.serialisation.*;
import java.nio.*;

public class AddCalendarEventRequestMessage extends OutputOnlyProxyMessage
{
    private WakfuCalendarEvent m_event;
    
    public void setEvent(final WakfuCalendarEvent event) {
        this.m_event = event;
    }
    
    @Override
    public byte[] encode() {
        final WakfuCalendarEventSerializer serializable = new WakfuCalendarEventSerializer();
        serializable.setEventForSerialisation(this.m_event);
        final ByteBuffer bb = ByteBuffer.allocate(serializable.expectedSize());
        serializable.serialize(bb);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 563;
    }
}
