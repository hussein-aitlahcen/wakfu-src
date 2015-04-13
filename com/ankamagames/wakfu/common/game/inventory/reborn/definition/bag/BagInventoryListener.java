package com.ankamagames.wakfu.common.game.inventory.reborn.definition.bag;

import com.ankamagames.wakfu.common.game.item.*;

public interface BagInventoryListener
{
    void bagAdded(Bag p0);
    
    void bagRemoved(Bag p0);
    
    void bagItemAdded(Item p0);
    
    void bagItemRemoved(Item p0);
}
