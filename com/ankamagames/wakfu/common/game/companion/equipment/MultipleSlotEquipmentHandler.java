package com.ankamagames.wakfu.common.game.companion.equipment;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public final class MultipleSlotEquipmentHandler implements InventoryObserver
{
    private static final Logger m_logger;
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        if (!(event instanceof InventoryItemModifiedEvent)) {
            return;
        }
        final InventoryItemModifiedEvent itemModifiedEvent = (InventoryItemModifiedEvent)event;
        final InventoryEvent.Action action = event.getAction();
        switch (action) {
            case ITEM_ADDED:
            case ITEM_ADDED_AT: {
                this.itemAdded(itemModifiedEvent.getConcernedItem(), itemModifiedEvent.getInventory());
                break;
            }
            case ITEM_REMOVED:
            case ITEM_REMOVED_AT: {
                this.itemRemoved(itemModifiedEvent.getConcernedItem(), itemModifiedEvent.getInventory());
            }
            case ITEM_QUANTITY_MODIFIED: {}
            case ITEM_PRICE_MODIFIED: {}
            case ITEM_PACK_SIZE_MODIFIED: {}
        }
    }
    
    private void itemRemoved(final InventoryContent content, final Inventory inventory) {
        this.changeLinkedPositionLock(content, inventory, false);
    }
    
    private void itemAdded(final InventoryContent content, final Inventory inventory) {
        this.changeLinkedPositionLock(content, inventory, true);
    }
    
    private void changeLinkedPositionLock(final InventoryContent content, final Inventory inventory, final boolean lock) {
        if (!(content instanceof Item)) {
            return;
        }
        if (!(inventory instanceof ItemEquipment)) {
            return;
        }
        final Item item = (Item)content;
        if (!item.isActive()) {
            return;
        }
        final ItemEquipment equipment = (ItemEquipment)inventory;
        final AbstractReferenceItem referenceItem = item.getReferenceItem();
        if (referenceItem == null) {
            return;
        }
        final AbstractItemType itemType = referenceItem.getItemType();
        if (itemType == null) {
            return;
        }
        final EquipmentPosition[] linkedPositions = itemType.getLinkedPositions();
        for (int i = 0, n = linkedPositions.length; i < n; ++i) {
            final EquipmentPosition linkedPosition = linkedPositions[i];
            if (lock) {
                try {
                    ((ArrayInventoryWithoutCheck<Item, R>)equipment).addAt(item.getInactiveCopy(), linkedPosition.getId());
                }
                catch (InventoryCapacityReachedException e) {
                    MultipleSlotEquipmentHandler.m_logger.error((Object)"Exception levee", (Throwable)e);
                }
                catch (ContentAlreadyPresentException e2) {
                    MultipleSlotEquipmentHandler.m_logger.error((Object)"Exception levee", (Throwable)e2);
                }
                catch (PositionAlreadyUsedException e3) {
                    MultipleSlotEquipmentHandler.m_logger.error((Object)"Exception levee", (Throwable)e3);
                }
            }
            else {
                equipment.removeAt(linkedPosition.getId());
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)MultipleSlotEquipmentHandler.class);
    }
}
