package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class NationRankBinaryData implements BinaryData
{
    protected int m_id;
    protected float m_citizenPointLossFactor;
    protected String m_criteria;
    protected int m_citizenScoreLine;
    
    public int getId() {
        return this.m_id;
    }
    
    public float getCitizenPointLossFactor() {
        return this.m_citizenPointLossFactor;
    }
    
    public String getCriteria() {
        return this.m_criteria;
    }
    
    public int getCitizenScoreLine() {
        return this.m_citizenScoreLine;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_citizenPointLossFactor = 0.0f;
        this.m_criteria = null;
        this.m_citizenScoreLine = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_citizenPointLossFactor = buffer.getFloat();
        this.m_criteria = buffer.readUTF8().intern();
        this.m_citizenScoreLine = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.NATION_RANK.getId();
    }
}
