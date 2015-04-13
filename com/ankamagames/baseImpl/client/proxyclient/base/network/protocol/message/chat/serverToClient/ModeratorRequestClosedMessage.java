package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ModeratorRequestClosedMessage extends InputOnlyProxyMessage
{
    private byte m_reason;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_reason = bb.get();
        return true;
    }
    
    public byte getReason() {
        return this.m_reason;
    }
    
    @Override
    public int getId() {
        return 3182;
    }
}
