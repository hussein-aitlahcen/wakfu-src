package com.ankamagames.wakfu.client.core.game.bookcase;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class BookcaseView extends ImmutableFieldProvider implements InventoryObserver
{
    public static final String SIZE = "size";
    public static final String INVENTORY = "inventory";
    public static final String EDITABLE = "editable";
    private Bookcase m_bookcase;
    
    public BookcaseView(final Bookcase bookcase) {
        super();
        (this.m_bookcase = bookcase).addInventoryObserver(this);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("size")) {
            return this.m_bookcase.getBag().getMaximumSize();
        }
        if (fieldName.equals("inventory")) {
            final Bag bag = this.m_bookcase.getBag();
            final Object[] result = new Object[bag.getInventory().getMaximumSize()];
            final Item[] items = bag.getInventory().toArray(new Item[bag.getInventory().getMaximumSize()]);
            final ItemDisplayerImpl.ItemPlaceHolder placeHolder = new ItemDisplayerImpl.ItemPlaceHolder();
            for (int i = 0, size = items.length; i < size; ++i) {
                if (items[i] == null) {
                    result[i] = placeHolder;
                }
                else {
                    result[i] = items[i];
                }
            }
            return result;
        }
        if (fieldName.equals("editable")) {
            final ItemizableInfo itemizableInfo = this.m_bookcase.getItemizableInfo();
            return itemizableInfo.getOwnerId() == WakfuGameEntity.getInstance().getLocalPlayer().getId() || MRUActionUtils.canManageInHavenWorld(itemizableInfo.getOwnerId());
        }
        return null;
    }
    
    public Bookcase getBookcase() {
        return this.m_bookcase;
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "inventory");
    }
}
