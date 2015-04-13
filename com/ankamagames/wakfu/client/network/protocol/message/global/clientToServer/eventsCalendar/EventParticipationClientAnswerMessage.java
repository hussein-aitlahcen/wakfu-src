package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class EventParticipationClientAnswerMessage extends OutputOnlyProxyMessage
{
    public static final byte YES = 0;
    public static final byte NO = 1;
    private long m_eventUid;
    private byte m_answer;
    
    public void setEventUid(final long eventUid) {
        this.m_eventUid = eventUid;
    }
    
    public void setAnswer(final byte answer) {
        this.m_answer = answer;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(9);
        bb.putLong(this.m_eventUid);
        bb.put(this.m_answer);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 581;
    }
}
