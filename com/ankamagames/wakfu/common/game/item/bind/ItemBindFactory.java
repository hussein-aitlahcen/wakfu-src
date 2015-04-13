package com.ankamagames.wakfu.common.game.item.bind;

public final class ItemBindFactory
{
    public static ItemBind create(final ItemBindType type) {
        return new ItemBindModel(type);
    }
    
    public static ItemBind create(final ItemBindType type, final long data) {
        return new ItemBindModel(type, data);
    }
}
