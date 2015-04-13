package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ItemTypeBinaryData implements BinaryData
{
    protected short m_id;
    protected short m_parentId;
    protected boolean m_visibleInAnimations;
    protected boolean m_visibleInMarketPlace;
    protected boolean m_recyclable;
    protected String[] m_equipmentPosition;
    protected String[] m_disabledEquipementPosition;
    protected short m_materialType;
    protected int[] m_craftIds;
    
    public short getId() {
        return this.m_id;
    }
    
    public short getParentId() {
        return this.m_parentId;
    }
    
    public boolean isVisibleInAnimations() {
        return this.m_visibleInAnimations;
    }
    
    public boolean isVisibleInMarketPlace() {
        return this.m_visibleInMarketPlace;
    }
    
    public boolean isRecyclable() {
        return this.m_recyclable;
    }
    
    public String[] getEquipmentPosition() {
        return this.m_equipmentPosition;
    }
    
    public String[] getDisabledEquipementPosition() {
        return this.m_disabledEquipementPosition;
    }
    
    public short getMaterialType() {
        return this.m_materialType;
    }
    
    public int[] getCraftIds() {
        return this.m_craftIds;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_parentId = 0;
        this.m_visibleInAnimations = false;
        this.m_visibleInMarketPlace = false;
        this.m_recyclable = false;
        this.m_equipmentPosition = null;
        this.m_disabledEquipementPosition = null;
        this.m_materialType = 0;
        this.m_craftIds = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getShort();
        this.m_parentId = buffer.getShort();
        this.m_visibleInAnimations = buffer.readBoolean();
        this.m_visibleInMarketPlace = buffer.readBoolean();
        this.m_recyclable = buffer.readBoolean();
        this.m_equipmentPosition = buffer.readStringArray();
        this.m_disabledEquipementPosition = buffer.readStringArray();
        this.m_materialType = buffer.getShort();
        this.m_craftIds = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ITEM_TYPE.getId();
    }
}
