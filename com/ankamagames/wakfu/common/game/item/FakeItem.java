package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class FakeItem extends Item
{
    private short m_quantity;
    private long m_uniqueId;
    
    public FakeItem(final AbstractReferenceItem referenceItem) {
        super(0L);
        this.initializeWithReferenceItem(referenceItem);
    }
    
    @Override
    public short getQuantity() {
        return this.m_quantity;
    }
    
    @Override
    public long getUniqueId() {
        return this.m_uniqueId;
    }
    
    public void setUniqueId(final long uniqueId) {
        this.m_uniqueId = uniqueId;
    }
    
    @Override
    public void setQuantity(final short quantity) {
        this.m_quantity = quantity;
        final ItemQuantityChangeListener quantityChangeListener = FakeItem.m_itemComposer.getQuantityChangeListener();
        if (quantityChangeListener != null) {
            quantityChangeListener.onQuantityChanged(this);
        }
    }
}
