package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public class Territory extends AbstractTerritory<CTerritoryChaosHandler>
{
    private int m_instanceId;
    private String m_name;
    private PartitionList m_partitions;
    private short m_minLevel;
    private short m_maxLevel;
    
    public Territory() {
        super(-1);
        this.m_name = null;
        this.m_partitions = null;
        this.setChaosHandler(new CTerritoryChaosHandler(this));
    }
    
    public Territory(final int id, final String name, final PartitionList partitions, final short minLevel, final short maxLevel) {
        super(id);
        this.m_name = name;
        this.m_partitions = partitions;
        this.m_minLevel = minLevel;
        this.m_maxLevel = maxLevel;
        this.setChaosHandler(new CTerritoryChaosHandler(this));
    }
    
    public int getInstanceId() {
        return this.m_instanceId;
    }
    
    public void setInstanceId(final int instanceId) {
        this.m_instanceId = instanceId;
    }
    
    public void read(final ByteBuffer buffer) {
        this.setId(buffer.getInt());
        final byte[] name = new byte[buffer.getShort() & 0xFFFF];
        buffer.get(name);
        this.m_name = StringUtils.fromUTF8(name);
        (this.m_partitions = new PartitionList()).readFrom(buffer);
        this.m_minLevel = buffer.getShort();
        this.m_maxLevel = buffer.getShort();
    }
    
    public void write(final OutputBitStream stream) throws IOException {
        stream.writeInt(this.getId());
        final byte[] bytes = StringUtils.toUTF8(this.m_name);
        stream.writeShort((short)bytes.length);
        stream.writeBytes(bytes);
        this.m_partitions.writeToStream(stream);
        stream.writeShort(this.m_minLevel);
        stream.writeShort(this.m_maxLevel);
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public PartitionList getPartitions() {
        return this.m_partitions;
    }
    
    public short getMinLevel() {
        return this.m_minLevel;
    }
    
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public boolean containsWorldPosition(final int x, final int y) {
        final int mx = PartitionConstants.getPartitionXFromCellX(x);
        final int my = PartitionConstants.getPartitionYFromCellY(y);
        return this.m_partitions.contains(mx, my);
    }
}
