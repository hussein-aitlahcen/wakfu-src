package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class DimensionalBagSpawnMessage extends InputOnlyProxyMessage
{
    private int m_worldX;
    private int m_worldY;
    private short m_altitude;
    private byte[] m_partialySerializedDimensionalBag;
    
    public int getWorldX() {
        return this.m_worldX;
    }
    
    public int getWorldY() {
        return this.m_worldY;
    }
    
    public short getAltitude() {
        return this.m_altitude;
    }
    
    public byte[] getPartialySerializedDimensionalBag() {
        return this.m_partialySerializedDimensionalBag;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_worldX = buffer.getInt();
        this.m_worldY = buffer.getInt();
        this.m_altitude = buffer.getShort();
        buffer.get(this.m_partialySerializedDimensionalBag = new byte[buffer.getShort() & 0xFFFF]);
        return true;
    }
    
    @Override
    public int getId() {
        return 10000;
    }
}
