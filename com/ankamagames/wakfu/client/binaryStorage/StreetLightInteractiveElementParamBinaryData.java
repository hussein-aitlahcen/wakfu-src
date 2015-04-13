package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class StreetLightInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_color;
    protected float m_range;
    protected int m_apsId;
    protected boolean m_activeOnlyInNight;
    protected int m_ignitionVisualId;
    protected boolean m_ignitionUseObject;
    protected int m_ignitionDuration;
    protected int m_extinctionVisualId;
    protected boolean m_extinctionUseObject;
    protected int m_extinctionDuration;
    protected ChaosParamBinaryData m_chaosParams;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getColor() {
        return this.m_color;
    }
    
    public float getRange() {
        return this.m_range;
    }
    
    public int getApsId() {
        return this.m_apsId;
    }
    
    public boolean isActiveOnlyInNight() {
        return this.m_activeOnlyInNight;
    }
    
    public int getIgnitionVisualId() {
        return this.m_ignitionVisualId;
    }
    
    public boolean isIgnitionUseObject() {
        return this.m_ignitionUseObject;
    }
    
    public int getIgnitionDuration() {
        return this.m_ignitionDuration;
    }
    
    public int getExtinctionVisualId() {
        return this.m_extinctionVisualId;
    }
    
    public boolean isExtinctionUseObject() {
        return this.m_extinctionUseObject;
    }
    
    public int getExtinctionDuration() {
        return this.m_extinctionDuration;
    }
    
    public ChaosParamBinaryData getChaosParams() {
        return this.m_chaosParams;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_color = 0;
        this.m_range = 0.0f;
        this.m_apsId = 0;
        this.m_activeOnlyInNight = false;
        this.m_ignitionVisualId = 0;
        this.m_ignitionUseObject = false;
        this.m_ignitionDuration = 0;
        this.m_extinctionVisualId = 0;
        this.m_extinctionUseObject = false;
        this.m_extinctionDuration = 0;
        this.m_chaosParams = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_color = buffer.getInt();
        this.m_range = buffer.getFloat();
        this.m_apsId = buffer.getInt();
        this.m_activeOnlyInNight = buffer.readBoolean();
        this.m_ignitionVisualId = buffer.getInt();
        this.m_ignitionUseObject = buffer.readBoolean();
        this.m_ignitionDuration = buffer.getInt();
        this.m_extinctionVisualId = buffer.getInt();
        this.m_extinctionUseObject = buffer.readBoolean();
        this.m_extinctionDuration = buffer.getInt();
        if (buffer.get() != 0) {
            (this.m_chaosParams = new ChaosParamBinaryData()).read(buffer);
        }
        else {
            this.m_chaosParams = null;
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.STREET_LIGHT_IE_PARAM.getId();
    }
}
