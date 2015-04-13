package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.merchant;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DimensionalBagFleaBuyRequestMessage extends OutputOnlyProxyMessage
{
    private long m_itemUid;
    private short m_quantity;
    
    public void setItemUid(final long itemUid) {
        this.m_itemUid = itemUid;
    }
    
    public void setQuantity(final short quantity) {
        this.m_quantity = quantity;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putLong(this.m_itemUid);
        buffer.putShort(this.m_quantity);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 10117;
    }
}
