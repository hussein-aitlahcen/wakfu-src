package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AchievementListBinaryData implements BinaryData
{
    protected int m_id;
    protected AchievementListData[] m_elements;
    
    public int getId() {
        return this.m_id;
    }
    
    public AchievementListData[] getElements() {
        return this.m_elements;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_elements = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        final int elementCount = buffer.getInt();
        this.m_elements = new AchievementListData[elementCount];
        for (int iElement = 0; iElement < elementCount; ++iElement) {
            (this.m_elements[iElement] = new AchievementListData()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ACHIEVEMENT_LIST.getId();
    }
    
    public static class AchievementListData
    {
        protected int m_achievementId;
        protected int m_order;
        
        public int getAchievementId() {
            return this.m_achievementId;
        }
        
        public int getOrder() {
            return this.m_order;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_achievementId = buffer.getInt();
            this.m_order = buffer.getInt();
        }
    }
}
