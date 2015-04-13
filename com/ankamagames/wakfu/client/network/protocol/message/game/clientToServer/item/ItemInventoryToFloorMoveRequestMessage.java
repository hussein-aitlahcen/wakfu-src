package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import java.nio.*;

public class ItemInventoryToFloorMoveRequestMessage extends InventoryMoveRequestMessage
{
    private short m_quantity;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 18;
        final ByteBuffer buffer = ByteBuffer.allocate(18);
        buffer.putLong(this.m_uid);
        buffer.putLong(this.m_source);
        buffer.putShort(this.m_quantity);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5209;
    }
    
    public void setQuantity(final short quantity) {
        this.m_quantity = quantity;
    }
}
