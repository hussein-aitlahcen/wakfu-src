package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ZaapBinaryData implements BinaryData
{
    protected int m_zaapId;
    protected int m_exitX;
    protected int m_exitY;
    protected int m_exitWorldId;
    protected int m_visualId;
    protected int m_uiGfxId;
    protected byte m_landmarkTravelType;
    protected boolean m_zaapBase;
    protected String m_destinationCriteria;
    protected TravelLoadingBinaryData m_loading;
    
    public int getZaapId() {
        return this.m_zaapId;
    }
    
    public int getExitX() {
        return this.m_exitX;
    }
    
    public int getExitY() {
        return this.m_exitY;
    }
    
    public int getExitWorldId() {
        return this.m_exitWorldId;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public int getUiGfxId() {
        return this.m_uiGfxId;
    }
    
    public byte getLandmarkTravelType() {
        return this.m_landmarkTravelType;
    }
    
    public boolean isZaapBase() {
        return this.m_zaapBase;
    }
    
    public String getDestinationCriteria() {
        return this.m_destinationCriteria;
    }
    
    public TravelLoadingBinaryData getLoading() {
        return this.m_loading;
    }
    
    @Override
    public void reset() {
        this.m_zaapId = 0;
        this.m_exitX = 0;
        this.m_exitY = 0;
        this.m_exitWorldId = 0;
        this.m_visualId = 0;
        this.m_uiGfxId = 0;
        this.m_landmarkTravelType = 0;
        this.m_zaapBase = false;
        this.m_destinationCriteria = null;
        this.m_loading = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_zaapId = buffer.getInt();
        this.m_exitX = buffer.getInt();
        this.m_exitY = buffer.getInt();
        this.m_exitWorldId = buffer.getInt();
        this.m_visualId = buffer.getInt();
        this.m_uiGfxId = buffer.getInt();
        this.m_landmarkTravelType = buffer.get();
        this.m_zaapBase = buffer.readBoolean();
        this.m_destinationCriteria = buffer.readUTF8().intern();
        if (buffer.get() != 0) {
            (this.m_loading = new TravelLoadingBinaryData()).read(buffer);
        }
        else {
            this.m_loading = null;
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ZAAP.getId();
    }
}
