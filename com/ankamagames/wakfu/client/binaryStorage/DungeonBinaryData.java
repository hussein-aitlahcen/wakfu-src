package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class DungeonBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_minLevel;
    protected short m_instanceId;
    protected int[] m_tps;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getMinLevel() {
        return this.m_minLevel;
    }
    
    public short getInstanceId() {
        return this.m_instanceId;
    }
    
    public int[] getTps() {
        return this.m_tps;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_minLevel = 0;
        this.m_instanceId = 0;
        this.m_tps = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_minLevel = buffer.getShort();
        this.m_instanceId = buffer.getShort();
        this.m_tps = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.DUNGEON.getId();
    }
}
