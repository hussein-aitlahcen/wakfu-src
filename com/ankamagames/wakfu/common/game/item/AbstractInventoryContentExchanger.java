package com.ankamagames.wakfu.common.game.item;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public abstract class AbstractInventoryContentExchanger<SourceInventoryType extends Inventory<Item>, TargetInventoryType extends Inventory<Item>> implements InventoryContentExchanger<Item, SourceInventoryType, TargetInventoryType>
{
    protected static final boolean DEBUG_MODE = false;
    public static final int OPERATION_FORBIDDEN = -1;
    public static final int OPERATION_SUCCESSFUL = 0;
    public static final int OPERATION_FAILED = 1;
    protected static final Logger m_logger;
    
    protected boolean addItemToEquipment(final Item item, final ItemEquipment equipment, final short position) throws ContentAlreadyPresentException, PositionAlreadyUsedException, InventoryCapacityReachedException {
        boolean ok;
        try {
            ok = ((ArrayInventoryWithoutCheck<Item, R>)equipment).addAt(item, position);
            if (item.getReferenceItem().getItemType().getLinkedPositions() != null) {
                for (final EquipmentPosition pos : item.getReferenceItem().getItemType().getLinkedPositions()) {
                    final Item tempItem = item.getInactiveCopy();
                    ok &= ((ArrayInventoryWithoutCheck<Item, R>)equipment).addAt(tempItem, pos.getId());
                }
            }
        }
        catch (PositionAlreadyUsedException e) {
            ((ArrayInventoryWithoutCheck<Item, R>)equipment).remove(item);
            for (final EquipmentPosition pos2 : item.getReferenceItem().getItemType().getLinkedPositions()) {
                final Item temp = ((ArrayInventoryWithoutCheck<Item, R>)equipment).removeAt(pos2.getId());
                if (temp != null) {
                    temp.release();
                }
            }
            return false;
        }
        return ok;
    }
    
    protected boolean removeFromEquipment(final Item item, final ItemEquipment equipment) {
        final boolean ok = ((ArrayInventoryWithoutCheck<Item, R>)equipment).remove(item);
        if (item.getReferenceItem().getItemType().getLinkedPositions() != null) {
            for (final EquipmentPosition pos : item.getReferenceItem().getItemType().getLinkedPositions()) {
                final Item temp = ((ArrayInventoryWithoutCheck<Item, R>)equipment).removeAt(pos.getId());
                if (temp != null) {
                    temp.release();
                }
            }
        }
        return ok;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractInventoryContentExchanger.class);
    }
}
