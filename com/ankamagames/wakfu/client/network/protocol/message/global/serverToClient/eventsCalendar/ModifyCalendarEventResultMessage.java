package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.serialisation.*;

public class ModifyCalendarEventResultMessage extends InputOnlyProxyMessage
{
    private byte m_errorCode;
    private WakfuCalendarEvent m_event;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_errorCode = bb.get();
        final WakfuCalendarEventSerializer serializable = new WakfuCalendarEventSerializer();
        serializable.unserialize(bb);
        this.m_event = serializable.getEvent();
        return true;
    }
    
    public byte getErrorCode() {
        return this.m_errorCode;
    }
    
    public WakfuCalendarEvent getEvent() {
        return this.m_event;
    }
    
    @Override
    public int getId() {
        return 596;
    }
}
