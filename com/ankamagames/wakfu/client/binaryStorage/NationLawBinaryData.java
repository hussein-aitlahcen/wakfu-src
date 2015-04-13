package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class NationLawBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_lawConstantId;
    protected String[] m_params;
    protected int m_basePointsModification;
    protected int m_percentPointsModification;
    protected int m_lawPointCost;
    protected boolean m_lawLocked;
    protected boolean m_applicableToCitizen;
    protected boolean m_applicableToAlliedForeigner;
    protected boolean m_applicableToNeutralForeigner;
    protected int[] m_restrictedNations;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getLawConstantId() {
        return this.m_lawConstantId;
    }
    
    public String[] getParams() {
        return this.m_params;
    }
    
    public int getBasePointsModification() {
        return this.m_basePointsModification;
    }
    
    public int getPercentPointsModification() {
        return this.m_percentPointsModification;
    }
    
    public int getLawPointCost() {
        return this.m_lawPointCost;
    }
    
    public boolean isLawLocked() {
        return this.m_lawLocked;
    }
    
    public boolean isApplicableToCitizen() {
        return this.m_applicableToCitizen;
    }
    
    public boolean isApplicableToAlliedForeigner() {
        return this.m_applicableToAlliedForeigner;
    }
    
    public boolean isApplicableToNeutralForeigner() {
        return this.m_applicableToNeutralForeigner;
    }
    
    public int[] getRestrictedNations() {
        return this.m_restrictedNations;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_lawConstantId = 0;
        this.m_params = null;
        this.m_basePointsModification = 0;
        this.m_percentPointsModification = 0;
        this.m_lawPointCost = 0;
        this.m_lawLocked = false;
        this.m_applicableToCitizen = false;
        this.m_applicableToAlliedForeigner = false;
        this.m_applicableToNeutralForeigner = false;
        this.m_restrictedNations = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_lawConstantId = buffer.getInt();
        this.m_params = buffer.readStringArray();
        this.m_basePointsModification = buffer.getInt();
        this.m_percentPointsModification = buffer.getInt();
        this.m_lawPointCost = buffer.getInt();
        this.m_lawLocked = buffer.readBoolean();
        this.m_applicableToCitizen = buffer.readBoolean();
        this.m_applicableToAlliedForeigner = buffer.readBoolean();
        this.m_applicableToNeutralForeigner = buffer.readBoolean();
        this.m_restrictedNations = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.NATION_LAW.getId();
    }
}
