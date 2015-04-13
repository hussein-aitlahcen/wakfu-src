package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class DestructibleElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_pdv;
    protected int m_regenDelay;
    protected int m_resWater;
    protected int m_resFire;
    protected int m_resEarth;
    protected int m_resWind;
    protected int[] m_effectIds;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getPdv() {
        return this.m_pdv;
    }
    
    public int getRegenDelay() {
        return this.m_regenDelay;
    }
    
    public int getResWater() {
        return this.m_resWater;
    }
    
    public int getResFire() {
        return this.m_resFire;
    }
    
    public int getResEarth() {
        return this.m_resEarth;
    }
    
    public int getResWind() {
        return this.m_resWind;
    }
    
    public int[] getEffectIds() {
        return this.m_effectIds;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_pdv = 0;
        this.m_regenDelay = 0;
        this.m_resWater = 0;
        this.m_resFire = 0;
        this.m_resEarth = 0;
        this.m_resWind = 0;
        this.m_effectIds = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_pdv = buffer.getInt();
        this.m_regenDelay = buffer.getInt();
        this.m_resWater = buffer.getInt();
        this.m_resFire = buffer.getInt();
        this.m_resEarth = buffer.getInt();
        this.m_resWind = buffer.getInt();
        this.m_effectIds = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.DESTRUCTIBLE_IE_PARAM.getId();
    }
}
