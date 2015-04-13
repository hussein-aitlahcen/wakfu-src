package com.ankamagames.wakfu.common.game.item.loot;

public interface Dropper
{
    LootList getBaseLootList();
    
    LootList getWorldLootList();
    
    LootList getFullLootList();
}
