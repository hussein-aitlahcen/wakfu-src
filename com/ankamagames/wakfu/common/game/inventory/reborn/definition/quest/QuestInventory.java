package com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;

public interface QuestInventory extends Inventory<QuestItem>
{
    QuestItem getItem(int p0);
    
    boolean addListener(QuestInventoryListener p0);
    
    boolean removeListener(QuestInventoryListener p0);
}
