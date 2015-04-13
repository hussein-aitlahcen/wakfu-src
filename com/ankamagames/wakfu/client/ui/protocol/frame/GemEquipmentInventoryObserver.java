package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.common.game.item.*;

class GemEquipmentInventoryObserver implements InventoryObserver
{
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        if (event.getAction() == InventoryEvent.Action.ITEM_ADDED || event.getAction() == InventoryEvent.Action.ITEM_ADDED_AT) {
            final InventoryItemModifiedEvent e = (InventoryItemModifiedEvent)event;
            final Item addedItem = (Item)e.getConcernedItem();
            if (addedItem.getUniqueId() == UIGemItemsFrame.getInstance().getCurrentItemId()) {
                UIGemItemsFrame.getInstance().setCurrentItem(addedItem);
            }
        }
    }
}
