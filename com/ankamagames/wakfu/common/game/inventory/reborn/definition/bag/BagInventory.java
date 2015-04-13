package com.ankamagames.wakfu.common.game.inventory.reborn.definition.bag;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;

public interface BagInventory extends Inventory<Bag>
{
    Bag getBag(long p0);
    
    boolean addListener(BagInventoryListener p0);
    
    boolean removeListener(BagInventoryListener p0);
}
