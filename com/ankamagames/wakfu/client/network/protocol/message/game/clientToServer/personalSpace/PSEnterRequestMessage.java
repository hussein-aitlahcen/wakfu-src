package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class PSEnterRequestMessage extends OutputOnlyProxyMessage
{
    private long m_worldObjectId;
    
    public void setWorldObjectId(final long worldObjectId) {
        this.m_worldObjectId = worldObjectId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(this.m_worldObjectId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 10003;
    }
}
