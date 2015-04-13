package com.ankamagames.wakfu.common.game.item.gems;

import com.ankamagames.wakfu.common.game.item.*;

public interface GemsHolder
{
    boolean hasGems();
    
    boolean hasGemsSlotted();
    
    Gems getGems() throws GemsException;
    
    long getUniqueId();
    
    short getLevel();
    
    ItemRarity getRarity();
}
