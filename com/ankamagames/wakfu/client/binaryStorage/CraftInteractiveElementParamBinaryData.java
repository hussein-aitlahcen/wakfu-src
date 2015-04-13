package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class CraftInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_apsId;
    protected int m_visualMruId;
    protected int m_skillId;
    protected int[] m_allowedRecipes;
    protected ChaosParamBinaryData m_chaosParams;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getApsId() {
        return this.m_apsId;
    }
    
    public int getVisualMruId() {
        return this.m_visualMruId;
    }
    
    public int getSkillId() {
        return this.m_skillId;
    }
    
    public int[] getAllowedRecipes() {
        return this.m_allowedRecipes;
    }
    
    public ChaosParamBinaryData getChaosParams() {
        return this.m_chaosParams;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_apsId = 0;
        this.m_visualMruId = 0;
        this.m_skillId = 0;
        this.m_allowedRecipes = null;
        this.m_chaosParams = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_apsId = buffer.getInt();
        this.m_visualMruId = buffer.getInt();
        this.m_skillId = buffer.getInt();
        this.m_allowedRecipes = buffer.readIntArray();
        if (buffer.get() != 0) {
            (this.m_chaosParams = new ChaosParamBinaryData()).read(buffer);
        }
        else {
            this.m_chaosParams = null;
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.CRAFT_IE_PARAM.getId();
    }
}
