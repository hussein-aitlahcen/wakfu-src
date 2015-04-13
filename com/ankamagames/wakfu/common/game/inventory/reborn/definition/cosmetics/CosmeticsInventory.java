package com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;

public interface CosmeticsInventory extends Inventory<CosmeticsItem>
{
    CosmeticsItem getItem(int p0);
    
    boolean addListener(CosmeticsInventoryListener p0);
    
    boolean removeListener(CosmeticsInventoryListener p0);
}
