package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class HavenWorldBoardInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_visualId;
    protected short m_havenWorldId;
    protected short m_miniOriginCellX;
    protected short m_miniOriginCellY;
    protected short m_miniOriginCellZ;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public short getHavenWorldId() {
        return this.m_havenWorldId;
    }
    
    public short getMiniOriginCellX() {
        return this.m_miniOriginCellX;
    }
    
    public short getMiniOriginCellY() {
        return this.m_miniOriginCellY;
    }
    
    public short getMiniOriginCellZ() {
        return this.m_miniOriginCellZ;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_visualId = 0;
        this.m_havenWorldId = 0;
        this.m_miniOriginCellX = 0;
        this.m_miniOriginCellY = 0;
        this.m_miniOriginCellZ = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_visualId = buffer.getInt();
        this.m_havenWorldId = buffer.getShort();
        this.m_miniOriginCellX = buffer.getShort();
        this.m_miniOriginCellY = buffer.getShort();
        this.m_miniOriginCellZ = buffer.getShort();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.HAVEN_WORLD_BOARD_IE_PARAM.getId();
    }
}
