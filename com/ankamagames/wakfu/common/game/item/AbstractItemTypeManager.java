package com.ankamagames.wakfu.common.game.item;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;

public abstract class AbstractItemTypeManager<ItemType extends AbstractItemType>
{
    protected static final Logger m_logger;
    protected final TIntObjectHashMap<ItemType> m_referenceItemTypes;
    
    protected AbstractItemTypeManager() {
        super();
        this.m_referenceItemTypes = new TIntObjectHashMap<ItemType>();
    }
    
    public void addItemType(final ItemType item) {
        this.m_referenceItemTypes.put(item.getId(), item);
    }
    
    public ItemType getItemType(final int itemTypeId) {
        return this.m_referenceItemTypes.get(itemTypeId);
    }
    
    public ArrayList<ItemType> getRootTypes() {
        final ArrayList<ItemType> rootTypes = new ArrayList<ItemType>(1);
        final TIntObjectIterator<ItemType> it = this.m_referenceItemTypes.iterator();
        while (it.hasNext()) {
            it.advance();
            final ItemType type = it.value();
            if (this.getItemType(type.getParentId()) == null) {
                rootTypes.add(type);
            }
        }
        return rootTypes;
    }
    
    public void updateItemTypeList() {
        final ArrayList<ItemType> rootTypes = new ArrayList<ItemType>(1);
        final TIntObjectIterator<ItemType> it = this.m_referenceItemTypes.iterator();
        while (it.hasNext()) {
            it.advance();
            final ItemType type = it.value();
            final ItemType parentType = this.getItemType(type.getParentId());
            type.setParentType(parentType);
            if (parentType == null) {
                rootTypes.add(type);
            }
            else {
                parentType.addChildType(type);
            }
        }
        for (int i = rootTypes.size() - 1; i >= 0; --i) {
            final ItemType type = rootTypes.get(i);
            this.updateChildsEquipmentPositions(type);
        }
    }
    
    private void updateChildsEquipmentPositions(final ItemType type) {
        if (!type.hasChilds()) {
            return;
        }
        final GrowingArray<ItemType> types = type.getChilds();
        for (int i = types.size() - 1; i >= 0; --i) {
            final ItemType child = types.get(i);
            final EquipmentPosition[] equipmentPositions = type.getEquipmentPositions();
            final EquipmentPosition[] childEquipmentPositions = child.getEquipmentPositions();
            if (equipmentPositions.length != 0 && childEquipmentPositions.length == 0) {
                child.setEquipmentPositions(equipmentPositions);
            }
            if (type.getLinkedPositions() != null && type.getLinkedPositions().length != 0 && (child.getLinkedPositions() == null || child.getLinkedPositions().length == 0)) {
                child.setLinkedPositions(type.getLinkedPositions());
            }
            this.updateChildsEquipmentPositions(child);
        }
    }
    
    public void updateItemTypesImbueTables() {
        final ArrayList<AbstractItemType> rootTypes = new ArrayList<AbstractItemType>(1);
        final TIntObjectIterator<ItemType> it = this.m_referenceItemTypes.iterator();
        while (it.hasNext()) {
            it.advance();
            final AbstractItemType type = it.value();
            final AbstractItemType parentType = type.getParentType();
            if (parentType == null) {
                rootTypes.add(type);
            }
        }
        for (int i = rootTypes.size() - 1; i >= 0; --i) {
            final AbstractItemType type = rootTypes.get(i);
            this.updateChildsImbueTables(type);
        }
    }
    
    private void updateChildsImbueTables(final AbstractItemType type) {
        if (!type.hasChilds()) {
            return;
        }
        final GrowingArray<ItemType> types = type.getChilds();
        for (int i = types.size() - 1; i >= 0; --i) {
            final ItemType child = types.get(i);
            this.updateChildsEquipmentPositions(child);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReferenceItemManager.class);
    }
}
