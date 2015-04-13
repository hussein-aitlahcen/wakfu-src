package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class AddCalendarEventAnswerMessage extends InputOnlyProxyMessage
{
    private byte m_errorCode;
    private long m_hashCode;
    private long m_newUid;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_errorCode = bb.get();
        this.m_hashCode = bb.getLong();
        this.m_newUid = bb.getLong();
        return true;
    }
    
    public byte getErrorCode() {
        return this.m_errorCode;
    }
    
    public long getAlternativeCode() {
        return this.m_hashCode;
    }
    
    public long getNewUid() {
        return this.m_newUid;
    }
    
    @Override
    public int getId() {
        return 564;
    }
}
