package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.dimensionalBag;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class RoomToInventoryGemExchangeResult extends InputOnlyProxyMessage
{
    private byte m_roomLayoutPosition;
    private boolean m_primary;
    private long m_inventoryUid;
    private byte m_position;
    
    public byte getRoomLayoutPosition() {
        return this.m_roomLayoutPosition;
    }
    
    public boolean isPrimary() {
        return this.m_primary;
    }
    
    public long getInventoryUid() {
        return this.m_inventoryUid;
    }
    
    public byte getPosition() {
        return this.m_position;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_inventoryUid = buffer.getLong();
        this.m_position = buffer.get();
        this.m_roomLayoutPosition = buffer.get();
        this.m_primary = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 10038;
    }
}
