package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class BagsBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_capacity;
    protected int m_bagType;
    protected int[] m_validItemCategories;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getCapacity() {
        return this.m_capacity;
    }
    
    public int getBagType() {
        return this.m_bagType;
    }
    
    public int[] getValidItemCategories() {
        return this.m_validItemCategories;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_capacity = 0;
        this.m_bagType = 0;
        this.m_validItemCategories = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_capacity = buffer.getShort();
        this.m_bagType = buffer.getInt();
        this.m_validItemCategories = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.BAGS.getId();
    }
}
