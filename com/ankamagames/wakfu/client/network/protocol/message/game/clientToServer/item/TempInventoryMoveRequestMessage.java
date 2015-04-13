package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import java.nio.*;

public class TempInventoryMoveRequestMessage extends InventoryMoveRequestMessage
{
    private short m_destinationPosition;
    private short m_quantity;
    private byte m_sourcePosition;
    
    public TempInventoryMoveRequestMessage() {
        super();
        this.m_uid = -1L;
        this.m_source = -1L;
        this.m_sourcePosition = -1;
        this.m_destinationPosition = -1;
        this.m_quantity = -1;
    }
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 21;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putLong(this.m_uid);
        buffer.putShort(this.m_quantity);
        buffer.put(this.m_sourcePosition);
        buffer.putLong(this.m_destinationId);
        buffer.putShort(this.m_destinationPosition);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5215;
    }
    
    public void setDestinationPosition(final short destinationPosition) {
        this.m_destinationPosition = destinationPosition;
    }
    
    public void setQuantity(final short quantity) {
        this.m_quantity = quantity;
    }
    
    public void setSourcePosition(final byte position) {
        this.m_sourcePosition = position;
    }
    
    @Override
    public void setSource(final long source) {
        TempInventoryMoveRequestMessage.m_logger.warn((Object)"On essaie d'indiquer une source pour un message provenant forc\u00e9ment de l'inventaire temporaire");
    }
}
