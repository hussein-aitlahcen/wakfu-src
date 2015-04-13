package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.eventsCalendar;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class SetEventDescMessage extends OutputOnlyProxyMessage
{
    private long m_eventUid;
    private String m_desc;
    
    public void setEventUid(final long eventUid) {
        this.m_eventUid = eventUid;
    }
    
    public void setDesc(final String desc) {
        this.m_desc = desc;
    }
    
    @Override
    public byte[] encode() {
        final byte[] descData = StringUtils.toUTF8(this.m_desc);
        final ByteBuffer bb = ByteBuffer.allocate(9 + descData.length);
        bb.putLong(this.m_eventUid);
        if (descData.length < 127) {
            bb.put((byte)descData.length);
            bb.put(descData);
        }
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 587;
    }
}
