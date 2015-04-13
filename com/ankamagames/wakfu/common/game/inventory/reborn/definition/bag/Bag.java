package com.ankamagames.wakfu.common.game.inventory.reborn.definition.bag;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;
import com.ankamagames.wakfu.common.game.item.*;

public interface Bag extends InventoryItem, Inventory<Item>
{
    long getId();
    
    Item getItem(long p0);
    
    Item getItemFromPosition(byte p0);
    
    boolean addListener(BagListener p0);
    
    boolean removeListener(BagListener p0);
    
    BagType getType();
}
