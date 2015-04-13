package com.ankamagames.wakfu.common.game.inventory.reborn.definition.temporary;

import com.ankamagames.wakfu.common.game.item.*;

public interface TemporaryInventoryListener
{
    void onItemAdded(Item p0);
    
    void onItemRemoved(Item p0);
    
    void onItemQuantityChanged(Item p0);
    
    void onClear();
}
