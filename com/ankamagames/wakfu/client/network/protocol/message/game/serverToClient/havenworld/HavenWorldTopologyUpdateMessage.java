package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class HavenWorldTopologyUpdateMessage extends InputOnlyProxyMessage
{
    private short m_partitionX;
    private short m_partitionY;
    private short m_topLeftPatch;
    private short m_topRightPatch;
    private short m_bottomLeftPatch;
    private short m_bottomRightPatch;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_partitionX = buffer.getShort();
        this.m_partitionY = buffer.getShort();
        this.m_topLeftPatch = buffer.getShort();
        this.m_topRightPatch = buffer.getShort();
        this.m_bottomLeftPatch = buffer.getShort();
        this.m_bottomRightPatch = buffer.getShort();
        return true;
    }
    
    public short getPartitionX() {
        return this.m_partitionX;
    }
    
    public short getPartitionY() {
        return this.m_partitionY;
    }
    
    public short getTopLeftPatch() {
        return this.m_topLeftPatch;
    }
    
    public short getTopRightPatch() {
        return this.m_topRightPatch;
    }
    
    public short getBottomLeftPatch() {
        return this.m_bottomLeftPatch;
    }
    
    public short getBottomRightPatch() {
        return this.m_bottomRightPatch;
    }
    
    @Override
    public int getId() {
        return 5510;
    }
}
