package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class HavenWorldBuildingEvolutionBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_catalogEntryId;
    protected int m_fromId;
    protected int m_toId;
    protected long m_delay;
    protected byte m_order;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getCatalogEntryId() {
        return this.m_catalogEntryId;
    }
    
    public int getFromId() {
        return this.m_fromId;
    }
    
    public int getToId() {
        return this.m_toId;
    }
    
    public long getDelay() {
        return this.m_delay;
    }
    
    public byte getOrder() {
        return this.m_order;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_catalogEntryId = 0;
        this.m_fromId = 0;
        this.m_toId = 0;
        this.m_delay = 0L;
        this.m_order = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_catalogEntryId = buffer.getShort();
        this.m_fromId = buffer.getInt();
        this.m_toId = buffer.getInt();
        this.m_delay = buffer.getLong();
        this.m_order = buffer.get();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.HAVEN_WORLD_BUILDING_EVOLUTION.getId();
    }
}
