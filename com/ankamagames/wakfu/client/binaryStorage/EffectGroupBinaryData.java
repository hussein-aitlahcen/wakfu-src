package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class EffectGroupBinaryData implements BinaryData
{
    protected int m_id;
    protected int[] m_effectIds;
    
    public int getId() {
        return this.m_id;
    }
    
    public int[] getEffectIds() {
        return this.m_effectIds;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_effectIds = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_effectIds = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.EFFECT_GROUP.getId();
    }
}
