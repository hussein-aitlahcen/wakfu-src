package com.ankamagames.wakfu.common.game.inventory.reborn.definition.bag;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;

public interface BagItem extends InventoryItem
{
    boolean addListener(BagItemListener p0);
    
    boolean removeListener(BagItemListener p0);
}
