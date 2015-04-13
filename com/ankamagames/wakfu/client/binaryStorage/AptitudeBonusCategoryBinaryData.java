package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AptitudeBonusCategoryBinaryData implements BinaryData
{
    protected int m_categoryId;
    protected int[] m_levels;
    protected int[] m_bonusIds;
    
    public int getCategoryId() {
        return this.m_categoryId;
    }
    
    public int[] getLevels() {
        return this.m_levels;
    }
    
    public int[] getBonusIds() {
        return this.m_bonusIds;
    }
    
    @Override
    public void reset() {
        this.m_categoryId = 0;
        this.m_levels = null;
        this.m_bonusIds = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_categoryId = buffer.getInt();
        this.m_levels = buffer.readIntArray();
        this.m_bonusIds = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.FIXED_ABILITY_CATEGORY.getId();
    }
}
