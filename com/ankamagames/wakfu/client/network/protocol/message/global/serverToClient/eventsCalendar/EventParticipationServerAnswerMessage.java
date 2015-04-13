package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class EventParticipationServerAnswerMessage extends InputOnlyProxyMessage
{
    private long m_eventId;
    private long m_invitedId;
    private String m_invitedName;
    private byte m_result;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_eventId = bb.getLong();
        this.m_invitedId = bb.getLong();
        final byte[] tInvitedName = new byte[bb.get()];
        bb.get(tInvitedName);
        this.m_invitedName = StringUtils.fromUTF8(tInvitedName);
        this.m_result = bb.get();
        return true;
    }
    
    public String getInvitedName() {
        return this.m_invitedName;
    }
    
    public byte getResult() {
        return this.m_result;
    }
    
    public long getEventId() {
        return this.m_eventId;
    }
    
    public long getInvitedId() {
        return this.m_invitedId;
    }
    
    @Override
    public int getId() {
        return 582;
    }
}
