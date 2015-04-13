package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.pet;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class PetFeedRequestMessage extends OutputOnlyProxyMessage
{
    private long m_petId;
    private long m_itemId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(this.m_petId);
        buffer.putLong(this.m_itemId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15983;
    }
    
    public void setPetId(final long petId) {
        this.m_petId = petId;
    }
    
    public void setItemId(final long itemId) {
        this.m_itemId = itemId;
    }
}
