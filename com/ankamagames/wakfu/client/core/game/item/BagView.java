package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class BagView extends AbstractBagView
{
    private final Bag m_bag;
    
    public BagView(final Bag bag) {
        super();
        this.m_bag = bag;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("bagName")) {
            return this.getName();
        }
        if (fieldName.equals("bagIconUrl")) {
            final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_bag.getReferenceId());
            if (referenceItem != null) {
                return WakfuConfiguration.getInstance().getItemSmallIconUrl(referenceItem.getGfxId());
            }
        }
        else {
            if (fieldName.equals("bagId")) {
                return this.m_bag.getUid();
            }
            if (fieldName.equals("bagInventory")) {
                final Object[] result = new Object[this.m_bag.getInventory().getMaximumSize()];
                final Item[] items = this.m_bag.getInventory().toArray(new Item[this.m_bag.getInventory().getMaximumSize()]);
                final ItemDisplayerImpl.ItemPlaceHolder placeHolder = new ItemDisplayerImpl.ItemPlaceHolder();
                for (int i = 0; i < items.length; ++i) {
                    if (items[i] == null) {
                        result[i] = placeHolder;
                    }
                    else {
                        result[i] = items[i];
                    }
                }
                return result;
            }
            if (fieldName.equals("bagNameSize")) {
                return this.getName() + " (" + this.m_bag.size() + "/" + this.m_bag.getMaximumSize() + ")";
            }
            if (fieldName.equals("bagSize")) {
                return this.m_bag.size();
            }
            if (fieldName.equals("bagPosition")) {
                return this.m_bag.getPosition();
            }
            if (fieldName.equals("canBeSorted")) {
                return true;
            }
        }
        return super.getFieldValue(fieldName);
    }
    
    private String getName() {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_bag.getReferenceId());
        if (referenceItem != null) {
            return referenceItem.getName();
        }
        return null;
    }
    
    public Bag getBag() {
        return this.m_bag;
    }
}
