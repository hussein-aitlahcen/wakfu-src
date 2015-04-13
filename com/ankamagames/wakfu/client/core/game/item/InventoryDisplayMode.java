package com.ankamagames.wakfu.client.core.game.item;

public enum InventoryDisplayMode
{
    BAGS((short)(-2)), 
    QUEST((short)385);
    
    protected final short m_iconId;
    
    private InventoryDisplayMode(final short iconId) {
        this.m_iconId = iconId;
    }
}
