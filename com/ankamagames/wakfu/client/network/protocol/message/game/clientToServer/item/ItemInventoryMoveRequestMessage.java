package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import java.nio.*;

public class ItemInventoryMoveRequestMessage extends InventoryMoveRequestMessage
{
    private short m_quantityMoved;
    private short m_destinationPosition;
    
    @Override
    public byte[] encode() {
        int sizeDatas = 27;
        if (this.m_newuid != 0L) {
            sizeDatas += 10;
        }
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putLong(this.m_uid);
        buffer.putLong(this.m_destinationId);
        buffer.putLong(this.m_source);
        buffer.putShort(this.m_destinationPosition);
        if (this.m_newuid != 0L) {
            buffer.put((byte)1);
            buffer.putLong(this.m_newuid);
            buffer.putShort(this.m_quantityMoved);
        }
        else {
            buffer.put((byte)0);
        }
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5213;
    }
    
    public void setQuantity(final short quantity) {
        this.m_quantityMoved = quantity;
    }
    
    public void setDestinationPosition(final short destinationPosition) {
        this.m_destinationPosition = destinationPosition;
    }
}
