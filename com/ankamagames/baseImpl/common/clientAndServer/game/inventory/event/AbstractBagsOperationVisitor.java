package com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import org.apache.log4j.*;

public abstract class AbstractBagsOperationVisitor<Item extends InventoryContent> implements InventoryObserver
{
    protected static final Logger m_logger;
    
    @Override
    public final void onInventoryEvent(final InventoryEvent event) {
        switch (event.getAction()) {
            case ITEM_ADDED:
            case ITEM_ADDED_AT: {
                final InventoryItemModifiedEvent ie = (InventoryItemModifiedEvent)event;
                this.itemAdded(ie.getConcernedItem());
            }
            case ITEM_REMOVED:
            case ITEM_REMOVED_AT: {
                final InventoryItemModifiedEvent ie = (InventoryItemModifiedEvent)event;
                this.itemRemoved(ie.getConcernedItem());
            }
            case ITEM_QUANTITY_MODIFIED: {
                final InventoryItemModifiedEvent ie = (InventoryItemModifiedEvent)event;
                this.itemQuantityUpdated(ie.getConcernedItem(), ie.getQuantity());
            }
            default: {
                throw new UnsupportedOperationException("Event " + event.getAction() + " non pris en compte par ce visiteur");
            }
        }
    }
    
    protected void itemAdded(final Item item) {
    }
    
    protected void itemRemoved(final Item item) {
    }
    
    protected void itemQuantityUpdated(final Item item, final short deltaQuantity) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractBagsOperationVisitor.class);
    }
}
