package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.framework.kernel.core.common.collections.*;
import org.apache.commons.lang3.*;
import org.jetbrains.annotations.*;

public abstract class AbstractItemType<ItemType extends AbstractItemType>
{
    private static final EquipmentPosition[] EMPTY_POSITION;
    private final short m_id;
    private ItemType m_parentType;
    private final short m_parentId;
    private final GrowingArray<ItemType> m_childsTypes;
    private EquipmentPosition[] m_equipmentPositions;
    private EquipmentPosition[] m_linkedPositions;
    private boolean m_isVisibleInMarketPlace;
    private boolean m_isRecyclable;
    private boolean m_isVisibleInAnimations;
    private final int[] m_craftIds;
    private final short m_materialType;
    
    protected AbstractItemType(final short id, final short parentId, final EquipmentPosition[] equipPositions, final int[] craftIds, final short materialType) {
        super();
        this.m_childsTypes = new GrowingArray<ItemType>();
        this.m_linkedPositions = null;
        this.m_isVisibleInMarketPlace = true;
        this.m_isRecyclable = true;
        this.m_isVisibleInAnimations = false;
        this.m_id = id;
        this.m_parentId = parentId;
        this.m_equipmentPositions = equipPositions;
        if (id == 611) {
            this.m_equipmentPositions = ArrayUtils.add(this.m_equipmentPositions, EquipmentPosition.MOUNT);
        }
        this.m_craftIds = craftIds;
        this.m_materialType = materialType;
    }
    
    public short getId() {
        return this.m_id;
    }
    
    public short getParentId() {
        return this.m_parentId;
    }
    
    public ItemType getParentType() {
        return this.m_parentType;
    }
    
    public void setParentType(final ItemType type) {
        this.m_parentType = type;
    }
    
    protected boolean hasChilds() {
        return this.m_childsTypes.size() != 0;
    }
    
    protected GrowingArray<ItemType> getChilds() {
        return this.m_childsTypes;
    }
    
    public void addChildType(final ItemType type) {
        this.m_childsTypes.add(type);
    }
    
    public boolean isChildOf(final AbstractItemType<ItemType> itemType) {
        if (this == itemType) {
            return true;
        }
        for (AbstractItemType<ItemType> parent = (AbstractItemType<ItemType>)this.m_parentType; parent != null; parent = (AbstractItemType<ItemType>)parent.getParentType()) {
            if (parent == itemType) {
                return true;
            }
        }
        return false;
    }
    
    @NotNull
    public EquipmentPosition[] getEquipmentPositions() {
        if (!this.hasEquipmentPositions() && this.getParentType() != null) {
            return this.getParentType().getEquipmentPositions();
        }
        return (this.m_equipmentPositions != null) ? this.m_equipmentPositions : AbstractItemType.EMPTY_POSITION;
    }
    
    public boolean hasEquipmentPositions() {
        return this.m_equipmentPositions != null && this.m_equipmentPositions.length > 0;
    }
    
    public int[] getCraftIds() {
        return this.m_craftIds;
    }
    
    public short getMaterialType() {
        return this.m_materialType;
    }
    
    public void setEquipmentPositions(final EquipmentPosition[] positions) {
        this.m_equipmentPositions = positions;
        if (this.m_id == 611) {
            this.m_equipmentPositions = ArrayUtils.add(this.m_equipmentPositions, EquipmentPosition.MOUNT);
        }
    }
    
    public boolean isEquipmentPositionValid(final EquipmentPosition position) {
        final EquipmentPosition[] equipmentPositions = this.getEquipmentPositions();
        for (int i = equipmentPositions.length - 1; i >= 0; --i) {
            if (equipmentPositions[i] == position) {
                return true;
            }
        }
        return false;
    }
    
    public EquipmentPosition[] getLinkedPositions() {
        return (this.m_linkedPositions != null) ? this.m_linkedPositions : AbstractItemType.EMPTY_POSITION;
    }
    
    public void setLinkedPositions(final EquipmentPosition[] linkedPositions) {
        this.m_linkedPositions = linkedPositions;
    }
    
    public boolean isVisibleInMarketPlace() {
        return this.m_isVisibleInMarketPlace;
    }
    
    public void setVisibleInMarketPlace(final boolean visibleInMarketPlace) {
        this.m_isVisibleInMarketPlace = visibleInMarketPlace;
    }
    
    public boolean isRecyclable() {
        return this.m_isRecyclable;
    }
    
    public void setRecyclable(final boolean recyclable) {
        this.m_isRecyclable = recyclable;
    }
    
    public boolean isOfType(final AbstractItemType type) {
        return type.getId() == this.m_id || (this.m_parentType != null && this.m_parentType.isOfType(type));
    }
    
    public boolean isVisibleInAnimations() {
        AbstractItemType parent = this;
        while (!parent.hasEquipmentPositions()) {
            parent = parent.getParentType();
            if (parent == null) {
                return false;
            }
        }
        return parent.m_isVisibleInAnimations;
    }
    
    public void setVisibleInAnimations(final boolean visibleInAnimations) {
        this.m_isVisibleInAnimations = visibleInAnimations;
    }
    
    static {
        EMPTY_POSITION = new EquipmentPosition[0];
    }
}
