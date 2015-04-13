package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class HavenWorldDefinitionBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_worldId;
    protected byte m_workers;
    protected short m_exitWorldId;
    protected short m_exitCellX;
    protected short m_exitCellY;
    protected short m_exitCellZ;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getWorldId() {
        return this.m_worldId;
    }
    
    public byte getWorkers() {
        return this.m_workers;
    }
    
    public short getExitWorldId() {
        return this.m_exitWorldId;
    }
    
    public short getExitCellX() {
        return this.m_exitCellX;
    }
    
    public short getExitCellY() {
        return this.m_exitCellY;
    }
    
    public short getExitCellZ() {
        return this.m_exitCellZ;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_worldId = 0;
        this.m_workers = 0;
        this.m_exitWorldId = 0;
        this.m_exitCellX = 0;
        this.m_exitCellY = 0;
        this.m_exitCellZ = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_worldId = buffer.getShort();
        this.m_workers = buffer.get();
        this.m_exitWorldId = buffer.getShort();
        this.m_exitCellX = buffer.getShort();
        this.m_exitCellY = buffer.getShort();
        this.m_exitCellZ = buffer.getShort();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.HAVEN_WORLD_DEFINITION.getId();
    }
}
