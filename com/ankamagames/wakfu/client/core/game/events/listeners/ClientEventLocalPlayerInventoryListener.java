package com.ankamagames.wakfu.client.core.game.events.listeners;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ClientEventLocalPlayerInventoryListener implements InventoryObserver
{
    private static final Logger m_logger;
    public static final ClientEventLocalPlayerInventoryListener INSTANCE;
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        switch (event.getAction()) {
            case ITEM_QUANTITY_MODIFIED: {
                final InventoryItemModifiedEvent modEvent = (InventoryItemModifiedEvent)event;
                if (modEvent.getQuantity() <= 0) {
                    return;
                }
            }
            case ITEM_ADDED:
            case ITEM_ADDED_AT: {
                final InventoryItemModifiedEvent modEvent = (InventoryItemModifiedEvent)event;
                final InventoryContent content = modEvent.getConcernedItem();
                if (!(content instanceof Item)) {
                    ClientEventLocalPlayerInventoryListener.m_logger.error((Object)("Ce listener ne doit \u00eatre utilis\u00e9 que pour des \u00e9v\u00e9nements d'Item et non de " + content.getClass()), (Throwable)new UnsupportedOperationException());
                    return;
                }
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventItemAddedToInventory(((Item)content).getReferenceItem()));
                break;
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientEventLocalPlayerInventoryListener.class);
        INSTANCE = new ClientEventLocalPlayerInventoryListener();
    }
}
