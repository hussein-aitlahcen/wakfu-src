package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class EventParticipationServerRequestMessage extends InputOnlyProxyMessage
{
    private long m_eventId;
    private String m_eventName;
    private String m_inviterName;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_eventId = bb.getLong();
        final byte[] tEventName = new byte[bb.get()];
        bb.get(tEventName);
        this.m_eventName = StringUtils.fromUTF8(tEventName);
        final byte[] tInviterName = new byte[bb.get()];
        bb.get(tInviterName);
        this.m_inviterName = StringUtils.fromUTF8(tInviterName);
        return true;
    }
    
    public String getEventName() {
        return this.m_eventName;
    }
    
    public String getInviterName() {
        return this.m_inviterName;
    }
    
    public long getEventId() {
        return this.m_eventId;
    }
    
    @Override
    public int getId() {
        return 580;
    }
}
