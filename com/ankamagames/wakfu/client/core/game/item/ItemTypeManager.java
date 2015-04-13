package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;

public class ItemTypeManager extends AbstractItemTypeManager<ItemType>
{
    private static final ItemTypeManager m_instance;
    private final BinaryLoader<ItemTypeBinaryData> m_creator;
    
    private ItemTypeManager() {
        super();
        this.m_creator = new BinaryLoaderFromFile<ItemTypeBinaryData>(new ItemTypeBinaryData());
    }
    
    public static ItemTypeManager getInstance() {
        return ItemTypeManager.m_instance;
    }
    
    @Nullable
    @Override
    public ItemType getItemType(final int itemTypeId) {
        if (itemTypeId == 0) {
            return null;
        }
        ItemType itemType = (ItemType)this.m_referenceItemTypes.get(itemTypeId);
        if (itemType == null && !this.m_referenceItemTypes.containsKey(itemTypeId)) {
            final ItemTypeBinaryData data = this.m_creator.createFromId(itemTypeId);
            if (data == null) {
                return null;
            }
            itemType = createItemTypeFromBinaryForm(data);
            if (itemType != null) {
                this.addItemType(itemType);
            }
        }
        return itemType;
    }
    
    public void addItemTypeFromBinaryForm(final ItemTypeBinaryData bs) {
        final ItemType item = createItemTypeFromBinaryForm(bs);
        this.addItemType(item);
    }
    
    @Nullable
    private static ItemType createItemTypeFromBinaryForm(final ItemTypeBinaryData bs) {
        if (bs == null) {
            return null;
        }
        final int itemTypeId = bs.getId();
        final int parentItemTypeId = bs.getParentId();
        final String[] positionsString = bs.getEquipmentPosition();
        final ArrayList<EquipmentPosition> equipmentPositions = new ArrayList<EquipmentPosition>();
        for (final String pos : positionsString) {
            if (pos != null && !pos.isEmpty()) {
                final EquipmentPosition position = EquipmentPosition.valueOf(pos);
                equipmentPositions.add(position);
            }
            else {
                ItemTypeManager.m_logger.error((Object)("Le slot [" + pos + "] n'est pas valide"));
            }
        }
        final String[] dPositionsString = bs.getDisabledEquipementPosition();
        final ArrayList<EquipmentPosition> disabledEqPositions = new ArrayList<EquipmentPosition>();
        for (final String pos2 : dPositionsString) {
            disabledEqPositions.add(EquipmentPosition.valueOf(pos2));
        }
        final EquipmentPosition[] positions = toArray(equipmentPositions);
        final EquipmentPosition[] disabledPositions = toArray(disabledEqPositions);
        final int[] craftIds = bs.getCraftIds();
        final short materialType = bs.getMaterialType();
        final ItemType type = new ItemType((short)itemTypeId, (short)parentItemTypeId, positions, craftIds, materialType);
        if (disabledPositions != null) {
            type.setLinkedPositions(disabledPositions);
        }
        type.setVisibleInMarketPlace(bs.isVisibleInMarketPlace());
        type.setRecyclable(bs.isRecyclable());
        type.setVisibleInAnimations(bs.isVisibleInAnimations());
        return type;
    }
    
    private static EquipmentPosition[] toArray(final ArrayList<EquipmentPosition> equipmentPositions) {
        if (equipmentPositions.size() > 0) {
            return equipmentPositions.toArray(new EquipmentPosition[equipmentPositions.size()]);
        }
        return null;
    }
    
    public boolean isEmpty() {
        return this.m_referenceItemTypes.isEmpty();
    }
    
    static {
        m_instance = new ItemTypeManager();
    }
}
