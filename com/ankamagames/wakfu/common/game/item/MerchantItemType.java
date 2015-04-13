package com.ankamagames.wakfu.common.game.item;

import gnu.trove.*;

public enum MerchantItemType
{
    ALL(new short[0]), 
    CONSUMABLE(new short[] { 106 }), 
    MISC(new short[] { 226 }), 
    STUFF(new short[] { 109 }), 
    ARTIFACT(new short[] { 385 }), 
    PROTECTOR_CHALLENGES(new short[] { 401 }), 
    PROTECTOR_BUFFS(new short[] { 402 }), 
    PROTECTOR_CLIMATE_BUFFS(new short[] { 403 });
    
    private TShortHashSet m_itemCategories;
    
    private MerchantItemType(final short[] categories) {
        (this.m_itemCategories = new TShortHashSet()).addAll(categories);
    }
    
    public boolean isValid(final AbstractItemType itemType) {
        if (this == MerchantItemType.ALL) {
            return true;
        }
        if (this.m_itemCategories.contains(itemType.getId())) {
            return true;
        }
        final AbstractItemType parent = itemType.getParentType();
        return parent != null && this.isValid(parent);
    }
    
    public TShortIterator getItemCategoriesIterator() {
        return this.m_itemCategories.iterator();
    }
}
