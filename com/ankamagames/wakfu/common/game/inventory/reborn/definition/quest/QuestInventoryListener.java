package com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest;

public interface QuestInventoryListener
{
    void itemAdded(QuestItem p0);
    
    void itemRemoved(QuestItem p0);
    
    void itemQuantityChanged(QuestItem p0, int p1);
}
