package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class NotificationIgnoreOfflineMessage extends InputOnlyProxyMessage
{
    private String m_ignoreName;
    private long m_userId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final byte[] fn = new byte[buffer.get() & 0xFF];
        buffer.get(fn);
        this.m_ignoreName = StringUtils.fromUTF8(fn);
        this.m_userId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 3166;
    }
    
    public String getIgnoreName() {
        return this.m_ignoreName;
    }
    
    public long getUserId() {
        return this.m_userId;
    }
}
