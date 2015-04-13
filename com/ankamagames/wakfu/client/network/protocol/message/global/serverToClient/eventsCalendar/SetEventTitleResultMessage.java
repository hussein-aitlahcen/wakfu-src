package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class SetEventTitleResultMessage extends InputOnlyProxyMessage
{
    private byte m_errorCode;
    private long m_eventId;
    private String m_oldDesc;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_errorCode = bb.get();
        this.m_eventId = bb.getLong();
        final byte descLength = bb.get();
        final byte[] descData = new byte[descLength];
        bb.get(descData);
        this.m_oldDesc = StringUtils.fromUTF8(descData);
        return true;
    }
    
    public byte getErrorCode() {
        return this.m_errorCode;
    }
    
    public long getEventId() {
        return this.m_eventId;
    }
    
    public String getOldDesc() {
        return this.m_oldDesc;
    }
    
    @Override
    public int getId() {
        return 590;
    }
}
