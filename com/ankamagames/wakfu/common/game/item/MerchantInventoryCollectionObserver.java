package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;

public interface MerchantInventoryCollectionObserver
{
    void onMerchantInventoryAdded(AbstractMerchantInventory p0);
    
    void onMerchantInventoryRemoved(AbstractMerchantInventory p0);
    
    void onMerchantInventoryUpdated(AbstractMerchantInventory p0, InventoryEvent p1);
}
