package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class SetEventMaxParticipantsOrRegistrationsMessage extends OutputOnlyProxyMessage
{
    public static final byte REGISTRATION = 1;
    public static final byte PARTICIPANT = 2;
    private long m_eventUid;
    private byte m_newMax;
    private byte m_type;
    
    public void setNewMax(final byte newMax) {
        this.m_newMax = newMax;
    }
    
    public void setEventUid(final long eventUid) {
        this.m_eventUid = eventUid;
    }
    
    public void setType(final byte type) {
        this.m_type = type;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(10);
        bb.putLong(this.m_eventUid);
        bb.put(this.m_newMax);
        bb.put(this.m_type);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 577;
    }
}
