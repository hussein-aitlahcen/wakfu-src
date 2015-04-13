package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class HasModerationRequestMessage extends InputOnlyProxyMessage
{
    private boolean m_hasRequests;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_hasRequests = (bb.get() != 0);
        return true;
    }
    
    public boolean hasRequests() {
        return this.m_hasRequests;
    }
    
    @Override
    public int getId() {
        return 3222;
    }
}
