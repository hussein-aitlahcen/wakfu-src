package com.ankamagames.wakfu.common.game.inventory.reborn.definition.temporary;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;
import com.ankamagames.wakfu.common.game.item.*;

public interface TemporaryInventory extends Inventory<Item>
{
    Item getByIdx(short p0);
    
    Item getById(long p0);
    
    boolean containsItemUid(long p0);
    
    boolean contains(Item p0);
    
    void clear();
    
    boolean isEmpty();
    
    boolean isFull();
    
    void addListener(TemporaryInventoryListener p0);
    
    void removeListener(TemporaryInventoryListener p0);
}
