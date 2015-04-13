package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import java.nio.*;

public class RemoveBagRequestMessage extends InventoryMoveRequestMessage
{
    private byte m_destinationPosition;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 33;
        final ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(this.m_uid);
        buffer.putLong(this.m_newuid);
        buffer.putLong(this.m_source);
        buffer.putLong(this.m_destinationId);
        buffer.put(this.m_destinationPosition);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5219;
    }
    
    public void setDestinationPosition(final byte destinationPosition) {
        this.m_destinationPosition = destinationPosition;
    }
}
