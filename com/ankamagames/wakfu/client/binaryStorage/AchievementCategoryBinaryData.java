package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AchievementCategoryBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_parentId;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getParentId() {
        return this.m_parentId;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_parentId = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_parentId = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ACHIEVEMENT_CATEGORY.getId();
    }
}
