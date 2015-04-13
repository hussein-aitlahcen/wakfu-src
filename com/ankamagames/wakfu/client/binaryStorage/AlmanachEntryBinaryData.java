package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AlmanachEntryBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_scenarioId;
    protected int m_achievementId;
    protected int[] m_territories;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getScenarioId() {
        return this.m_scenarioId;
    }
    
    public int getAchievementId() {
        return this.m_achievementId;
    }
    
    public int[] getTerritories() {
        return this.m_territories;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_scenarioId = 0;
        this.m_achievementId = 0;
        this.m_territories = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_scenarioId = buffer.getInt();
        this.m_achievementId = buffer.getInt();
        this.m_territories = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ALMANACH_ENTRY.getId();
    }
}
