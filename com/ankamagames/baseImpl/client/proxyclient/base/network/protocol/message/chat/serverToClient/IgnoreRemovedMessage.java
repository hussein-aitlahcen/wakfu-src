package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class IgnoreRemovedMessage extends InputOnlyProxyMessage
{
    private String m_ignoreName;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] cn = new byte[bb.get() & 0xFF];
        bb.get(cn);
        this.m_ignoreName = StringUtils.fromUTF8(cn);
        return true;
    }
    
    @Override
    public int getId() {
        return 3162;
    }
    
    public String getIgnoreName() {
        return this.m_ignoreName;
    }
}
