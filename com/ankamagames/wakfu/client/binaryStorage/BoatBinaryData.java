package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class BoatBinaryData implements BinaryData
{
    protected int m_boatId;
    protected int m_exitX;
    protected int m_exitY;
    protected int m_exitWorldId;
    protected int m_visualId;
    protected int m_uiGfxId;
    protected byte m_landmarkTravelType;
    
    public int getBoatId() {
        return this.m_boatId;
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
    
    @Override
    public void reset() {
        this.m_boatId = 0;
        this.m_exitX = 0;
        this.m_exitY = 0;
        this.m_exitWorldId = 0;
        this.m_visualId = 0;
        this.m_uiGfxId = 0;
        this.m_landmarkTravelType = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_boatId = buffer.getInt();
        this.m_exitX = buffer.getInt();
        this.m_exitY = buffer.getInt();
        this.m_exitWorldId = buffer.getInt();
        this.m_visualId = buffer.getInt();
        this.m_uiGfxId = buffer.getInt();
        this.m_landmarkTravelType = buffer.get();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.BOAT.getId();
    }
}
