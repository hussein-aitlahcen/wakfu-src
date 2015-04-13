package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.costume;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class UnlinkCostumeMessage extends OutputOnlyProxyMessage
{
    private int m_refItemId;
    
    public UnlinkCostumeMessage(final int refItemId) {
        super();
        this.m_refItemId = refItemId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(this.m_refItemId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15813;
    }
}
