package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DeleteItemRequestMessage extends OutputOnlyProxyMessage
{
    private long m_itemId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(this.m_itemId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5261;
    }
    
    public void setItemId(final long itemId) {
        this.m_itemId = itemId;
    }
}
