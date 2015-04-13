package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import java.nio.*;

public class AddBagRequestMessage extends InventoryMoveRequestMessage
{
    private byte m_destinationPosition;
    private long m_destinationCharacterId;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 25;
        final ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(this.m_uid);
        buffer.putLong(this.m_source);
        buffer.putLong(this.m_destinationCharacterId);
        buffer.put(this.m_destinationPosition);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5218;
    }
    
    public void setDestinationCharacterId(final long destinationCharacterId) {
        this.m_destinationCharacterId = destinationCharacterId;
    }
    
    public void setDestinationPosition(final byte destinationPosition) {
        this.m_destinationPosition = destinationPosition;
    }
}
