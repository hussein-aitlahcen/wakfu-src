package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ModeratorRequestErrorMessage extends InputOnlyProxyMessage
{
    private byte m_errorId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_errorId = bb.get();
        return true;
    }
    
    public byte getErrorId() {
        return this.m_errorId;
    }
    
    @Override
    public int getId() {
        return 3220;
    }
}
