package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class DragoBinaryData implements BinaryData
{
    protected int m_dragoId;
    protected int m_exitX;
    protected int m_exitY;
    protected int m_visualId;
    protected int m_uiGfxId;
    protected String m_dragoCriterion;
    protected byte m_landmarkTravelType;
    protected TravelLoadingBinaryData m_loading;
    
    public int getDragoId() {
        return this.m_dragoId;
    }
    
    public int getExitX() {
        return this.m_exitX;
    }
    
    public int getExitY() {
        return this.m_exitY;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public int getUiGfxId() {
        return this.m_uiGfxId;
    }
    
    public String getDragoCriterion() {
        return this.m_dragoCriterion;
    }
    
    public byte getLandmarkTravelType() {
        return this.m_landmarkTravelType;
    }
    
    public TravelLoadingBinaryData getLoading() {
        return this.m_loading;
    }
    
    @Override
    public void reset() {
        this.m_dragoId = 0;
        this.m_exitX = 0;
        this.m_exitY = 0;
        this.m_visualId = 0;
        this.m_uiGfxId = 0;
        this.m_dragoCriterion = null;
        this.m_landmarkTravelType = 0;
        this.m_loading = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_dragoId = buffer.getInt();
        this.m_exitX = buffer.getInt();
        this.m_exitY = buffer.getInt();
        this.m_visualId = buffer.getInt();
        this.m_uiGfxId = buffer.getInt();
        this.m_dragoCriterion = buffer.readUTF8().intern();
        this.m_landmarkTravelType = buffer.get();
        if (buffer.get() != 0) {
            (this.m_loading = new TravelLoadingBinaryData()).read(buffer);
        }
        else {
            this.m_loading = null;
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.DRAGO.getId();
    }
}
