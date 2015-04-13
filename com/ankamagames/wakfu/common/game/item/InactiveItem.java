package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class InactiveItem extends Item
{
    private Item m_baseItem;
    
    public InactiveItem(final long uid, final AbstractReferenceItem referenceItem, final Item baseItem) {
        super(uid);
        this.m_baseItem = baseItem;
        this.initializeWithReferenceItem(referenceItem);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_baseItem = null;
    }
    
    @Override
    public void release() {
        this.m_baseItem = null;
    }
    
    public Item getBaseItem() {
        return this.m_baseItem;
    }
    
    @Override
    public boolean isActive() {
        return false;
    }
}
