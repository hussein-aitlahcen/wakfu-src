package com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;

public interface QuestItem extends InventoryItem
{
    int getRefId();
    
    short getQuantity();
    
    short getStackMaximumHeight();
    
    boolean addListener(QuestItemListener p0);
    
    boolean removeListener(QuestItemListener p0);
}
