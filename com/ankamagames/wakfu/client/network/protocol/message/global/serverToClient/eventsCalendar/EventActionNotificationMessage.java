package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class EventActionNotificationMessage extends InputOnlyProxyMessage
{
    private byte m_actionId;
    private long m_eventId;
    private long m_performerId;
    private String m_performerName;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_actionId = bb.get();
        this.m_eventId = bb.getLong();
        this.m_performerId = bb.getLong();
        final byte descLength = bb.get();
        final byte[] descData = new byte[descLength];
        bb.get(descData);
        this.m_performerName = StringUtils.fromUTF8(descData);
        return true;
    }
    
    public long getEventId() {
        return this.m_eventId;
    }
    
    public String getPerformerName() {
        return this.m_performerName;
    }
    
    public long getPerformerId() {
        return this.m_performerId;
    }
    
    public byte getActionId() {
        return this.m_actionId;
    }
    
    @Override
    public int getId() {
        return 592;
    }
}
