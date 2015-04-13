package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class Bag extends AbstractBag implements InventoryObserver
{
    private final BagView m_bagView;
    
    public Bag(final long uid, final int referenceId, final InventoryContentChecker checker, final short maximumSize, final ClientBagContainer clientBagContainer) {
        super(uid, referenceId, checker, maximumSize);
        this.addObserver(this);
        this.m_bagView = new BagView(this);
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        switch (event.getAction()) {
            case CLEARED: {
                PropertiesProvider.getInstance().firePropertyValueChanged(this.m_bagView, "bagInventory", "bagSize", "bagNameSize");
                break;
            }
            case ITEM_ADDED_AT:
            case ITEM_ADDED: {
                PropertiesProvider.getInstance().firePropertyValueChanged(this.m_bagView, "bagInventory", "bagSize", "bagNameSize");
                if (this.getUid() != 2L) {
                    final InventoryItemModifiedEvent modEvent = (InventoryItemModifiedEvent)event;
                    if (modEvent.getConcernedItem() instanceof Item) {
                        final Item concernedItem = (Item)modEvent.getConcernedItem();
                        if (concernedItem.isActive()) {
                            changedQuantity(concernedItem);
                        }
                        if (concernedItem.hasPet()) {
                            UIPetFrame.getInstance().unloadPetDetailForItem(concernedItem);
                        }
                    }
                    WakfuSoundManager.getInstance().storeItem();
                    break;
                }
                break;
            }
            case ITEM_REMOVED_AT:
            case ITEM_REMOVED: {
                final InventoryItemModifiedEvent modEvent = (InventoryItemModifiedEvent)event;
                PropertiesProvider.getInstance().firePropertyValueChanged(this.m_bagView, "bagInventory", "bagSize", "bagNameSize");
                final InventoryContent concernedItem2 = modEvent.getConcernedItem();
                final Item itemDetailed = (Item)PropertiesProvider.getInstance().getObjectProperty("itemDetail", "equipmentDialog");
                if (itemDetailed != null && itemDetailed.equals(concernedItem2)) {
                    PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", null, "equipmentDialog");
                    PropertiesProvider.getInstance().setPropertyValue("pet", null);
                }
                if (this.getUid() != 2L) {
                    if (modEvent.getConcernedItem() instanceof Item) {
                        final Item concernedItem3 = (Item)concernedItem2;
                        if (concernedItem3.isActive()) {
                            changedQuantity(concernedItem3);
                            if (concernedItem3.hasPet()) {
                                UIPetFrame.getInstance().unloadPetDetailForItem(concernedItem3);
                            }
                        }
                    }
                    WakfuSoundManager.getInstance().dropItem();
                    break;
                }
                break;
            }
            case ITEM_QUANTITY_MODIFIED: {
                PropertiesProvider.getInstance().firePropertyValueChanged(this.m_bagView, "bagInventory");
                if (this.getUid() == 2L) {
                    break;
                }
                final InventoryItemModifiedEvent modEvent = (InventoryItemModifiedEvent)event;
                if (!(modEvent.getConcernedItem() instanceof Item)) {
                    break;
                }
                final Item concernedItem = (Item)modEvent.getConcernedItem();
                if (concernedItem.isActive()) {
                    concernedItem.resetCache();
                    break;
                }
                break;
            }
        }
    }
    
    private static void changedQuantity(final Item concernedItem2) {
        concernedItem2.resetCache();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null && localPlayer.getShortcutBarManager() != null) {
            localPlayer.getShortcutBarManager().onQuantityChanged(concernedItem2);
        }
    }
    
    public BagView getBagView() {
        return this.m_bagView;
    }
}
