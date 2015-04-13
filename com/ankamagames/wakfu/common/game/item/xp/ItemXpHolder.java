package com.ankamagames.wakfu.common.game.item.xp;

public interface ItemXpHolder
{
    boolean hasXp();
    
    ItemXp getXp() throws ItemXpException;
    
    long getUniqueId();
}
