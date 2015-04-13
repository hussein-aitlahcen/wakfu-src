package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ClimateBonusBinaryData implements BinaryData
{
    protected int m_buffId;
    protected int m_gfxId;
    protected String m_criteria;
    protected int m_duration;
    protected short m_price;
    protected float m_temperatureDifference;
    protected float m_rainDifference;
    protected float m_windDifference;
    
    public int getBuffId() {
        return this.m_buffId;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public String getCriteria() {
        return this.m_criteria;
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public short getPrice() {
        return this.m_price;
    }
    
    public float getTemperatureDifference() {
        return this.m_temperatureDifference;
    }
    
    public float getRainDifference() {
        return this.m_rainDifference;
    }
    
    public float getWindDifference() {
        return this.m_windDifference;
    }
    
    @Override
    public void reset() {
        this.m_buffId = 0;
        this.m_gfxId = 0;
        this.m_criteria = null;
        this.m_duration = 0;
        this.m_price = 0;
        this.m_temperatureDifference = 0.0f;
        this.m_rainDifference = 0.0f;
        this.m_windDifference = 0.0f;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_buffId = buffer.getInt();
        this.m_gfxId = buffer.getInt();
        this.m_criteria = buffer.readUTF8().intern();
        this.m_duration = buffer.getInt();
        this.m_price = buffer.getShort();
        this.m_temperatureDifference = buffer.getFloat();
        this.m_rainDifference = buffer.getFloat();
        this.m_windDifference = buffer.getFloat();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.CLIMATE_BONUS.getId();
    }
}
