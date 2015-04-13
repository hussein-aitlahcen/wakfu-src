package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class CannonBinaryData implements BinaryData
{
    protected int m_cannonId;
    protected int m_visualId;
    protected int m_uiGfxId;
    protected byte m_landmarkTravelType;
    protected int m_itemId;
    protected int m_itemQty;
    protected Link[] m_links;
    
    public int getCannonId() {
        return this.m_cannonId;
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
    
    public int getItemId() {
        return this.m_itemId;
    }
    
    public int getItemQty() {
        return this.m_itemQty;
    }
    
    public Link[] getLinks() {
        return this.m_links;
    }
    
    @Override
    public void reset() {
        this.m_cannonId = 0;
        this.m_visualId = 0;
        this.m_uiGfxId = 0;
        this.m_landmarkTravelType = 0;
        this.m_itemId = 0;
        this.m_itemQty = 0;
        this.m_links = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_cannonId = buffer.getInt();
        this.m_visualId = buffer.getInt();
        this.m_uiGfxId = buffer.getInt();
        this.m_landmarkTravelType = buffer.get();
        this.m_itemId = buffer.getInt();
        this.m_itemQty = buffer.getInt();
        final int linkCount = buffer.getInt();
        this.m_links = new Link[linkCount];
        for (int iLink = 0; iLink < linkCount; ++iLink) {
            (this.m_links[iLink] = new Link()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.CANNON.getId();
    }
    
    public static class Link
    {
        protected int m_id;
        protected int m_exitX;
        protected int m_exitY;
        protected int m_exitWorldId;
        protected int m_dropWeight;
        protected String m_criteria;
        protected TravelLoadingBinaryData m_loading;
        
        public int getId() {
            return this.m_id;
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
        
        public int getDropWeight() {
            return this.m_dropWeight;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public TravelLoadingBinaryData getLoading() {
            return this.m_loading;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_exitX = buffer.getInt();
            this.m_exitY = buffer.getInt();
            this.m_exitWorldId = buffer.getInt();
            this.m_dropWeight = buffer.getInt();
            this.m_criteria = buffer.readUTF8().intern();
            if (buffer.get() != 0) {
                (this.m_loading = new TravelLoadingBinaryData()).read(buffer);
            }
            else {
                this.m_loading = null;
            }
        }
    }
}
