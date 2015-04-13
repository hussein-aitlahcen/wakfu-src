package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.dimensionalBag;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RoomToInventoryGemExchange extends OutputOnlyProxyMessage
{
    private byte m_roomLayoutPosition;
    private boolean m_primary;
    private long m_inventoryUid;
    private byte m_position;
    
    public void setRoomLayoutPosition(final byte roomLayoutPosition) {
        this.m_roomLayoutPosition = roomLayoutPosition;
    }
    
    public void setPrimary(final boolean primary) {
        this.m_primary = primary;
    }
    
    public void setInventoryUid(final long inventoryUid) {
        this.m_inventoryUid = inventoryUid;
    }
    
    public void setPosition(final byte position) {
        this.m_position = position;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(11);
        buffer.putLong(this.m_inventoryUid);
        buffer.put(this.m_position);
        buffer.put(this.m_roomLayoutPosition);
        buffer.put((byte)(this.m_primary ? 1 : 0));
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 10037;
    }
}
