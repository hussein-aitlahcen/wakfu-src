package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class HavenWorldBuildingCatalogBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_order;
    protected int m_buildingType;
    protected int m_categoryId;
    protected boolean m_buyable;
    protected short m_maxQuantity;
    protected boolean m_isDecoOnly;
    protected int m_buildingSoundId;
    protected BuildingCondition[] m_buildingCondition;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getOrder() {
        return this.m_order;
    }
    
    public int getBuildingType() {
        return this.m_buildingType;
    }
    
    public int getCategoryId() {
        return this.m_categoryId;
    }
    
    public boolean isBuyable() {
        return this.m_buyable;
    }
    
    public short getMaxQuantity() {
        return this.m_maxQuantity;
    }
    
    public boolean isDecoOnly() {
        return this.m_isDecoOnly;
    }
    
    public int getBuildingSoundId() {
        return this.m_buildingSoundId;
    }
    
    public BuildingCondition[] getBuildingCondition() {
        return this.m_buildingCondition;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_order = 0;
        this.m_buildingType = 0;
        this.m_categoryId = 0;
        this.m_buyable = false;
        this.m_maxQuantity = 0;
        this.m_isDecoOnly = false;
        this.m_buildingSoundId = 0;
        this.m_buildingCondition = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_order = buffer.getInt();
        this.m_buildingType = buffer.getInt();
        this.m_categoryId = buffer.getInt();
        this.m_buyable = buffer.readBoolean();
        this.m_maxQuantity = buffer.getShort();
        this.m_isDecoOnly = buffer.readBoolean();
        this.m_buildingSoundId = buffer.getInt();
        final int buildingConditionCount = buffer.getInt();
        this.m_buildingCondition = new BuildingCondition[buildingConditionCount];
        for (int iBuildingCondition = 0; iBuildingCondition < buildingConditionCount; ++iBuildingCondition) {
            (this.m_buildingCondition[iBuildingCondition] = new BuildingCondition()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.HAVEN_WORLD_BUILDING_CATALOG.getId();
    }
    
    public static class BuildingCondition
    {
        protected int m_buildingTypeNeeded;
        protected int m_quantity;
        
        public int getBuildingTypeNeeded() {
            return this.m_buildingTypeNeeded;
        }
        
        public int getQuantity() {
            return this.m_quantity;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_buildingTypeNeeded = buffer.getInt();
            this.m_quantity = buffer.getInt();
        }
    }
}
