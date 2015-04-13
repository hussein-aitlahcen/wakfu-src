package com.ankamagames.wakfu.common.game.item.bind;

import com.ankamagames.wakfu.common.game.item.*;

public class ItemBindController
{
    private final Item m_item;
    
    public ItemBindController(final Item item) {
        super();
        this.m_item = item;
    }
    
    public void bind(final long data) throws ItemBindException {
        if (!this.m_item.hasBind()) {
            throw new ItemBindException("L'item ne peut pas \u00eatre li\u00e9");
        }
        if (this.m_item.isBound()) {
            throw new ItemBindException("L'item est d\u00e9j\u00e0 li\u00e9");
        }
        final ItemBindModel bind = (ItemBindModel)this.m_item.getBind();
        bind.setData(data);
    }
    
    public Item getItem() {
        return this.m_item;
    }
}
