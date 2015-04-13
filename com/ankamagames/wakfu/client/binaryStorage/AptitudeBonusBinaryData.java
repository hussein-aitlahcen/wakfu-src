package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AptitudeBonusBinaryData implements BinaryData
{
    protected int m_bonusId;
    protected int m_categoryId;
    protected int m_max;
    protected int m_gfxId;
    protected int[] m_effectIds;
    
    public int getBonusId() {
        return this.m_bonusId;
    }
    
    public int getCategoryId() {
        return this.m_categoryId;
    }
    
    public int getMax() {
        return this.m_max;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public int[] getEffectIds() {
        return this.m_effectIds;
    }
    
    @Override
    public void reset() {
        this.m_bonusId = 0;
        this.m_categoryId = 0;
        this.m_max = 0;
        this.m_gfxId = 0;
        this.m_effectIds = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_bonusId = buffer.getInt();
        this.m_categoryId = buffer.getInt();
        this.m_max = buffer.getInt();
        this.m_gfxId = buffer.getInt();
        this.m_effectIds = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ABILITY.getId();
    }
}
