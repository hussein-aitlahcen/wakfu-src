package com.ankamagames.wakfu.common.game.resource;

import org.apache.log4j.*;
import java.util.*;
import java.nio.*;

public final class ResourceDecoder
{
    protected static final Logger m_logger;
    private short m_partitionX;
    private short m_partitionY;
    private ArrayList<ResourceInfo> m_resources;
    
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        return this.decode(buffer);
    }
    
    public boolean decode(final ByteBuffer buffer) {
        this.m_partitionX = buffer.getShort();
        this.m_partitionY = buffer.getShort();
        final int nbDifferentResources = buffer.getShort() & 0xFFFF;
        this.m_resources = new ArrayList<ResourceInfo>(nbDifferentResources * 10);
        for (int i = 0; i < nbDifferentResources; ++i) {
            final short refId = buffer.getShort();
            for (int nResources = buffer.getShort() & 0xFFFF, j = 0; j < nResources; ++j) {
                final ResourceInfo resourceInfo = new ResourceInfo(refId);
                resourceInfo.unserialize(buffer.getInt(), this.m_partitionX, this.m_partitionY);
                this.m_resources.add(resourceInfo);
            }
        }
        return true;
    }
    
    public short getPartitionX() {
        return this.m_partitionX;
    }
    
    public short getPartitionY() {
        return this.m_partitionY;
    }
    
    public ArrayList<ResourceInfo> getResources() {
        return this.m_resources;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ResourceDecoder.class);
    }
}
