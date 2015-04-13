package com.ankamagames.wakfu.client.core.game.fight.history;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;

public class ReferenceItemFieldProvider extends ImmutableFieldProvider implements Comparable<ReferenceItemFieldProvider>
{
    public static final String QUANTITY_FIELD = "quantity";
    public static final String[] LOOTITEM_FIELDS;
    private final short m_quantity;
    private final ReferenceItem m_referenceItem;
    private final short m_level;
    private final ItemRarity m_rarity;
    
    public ReferenceItemFieldProvider(final int refId, final short quantity) {
        super();
        this.m_quantity = quantity;
        this.m_referenceItem = ReferenceItemManager.getInstance().getReferenceItem(refId);
        this.m_level = this.m_referenceItem.getLevel();
        this.m_rarity = this.m_referenceItem.getRarity();
    }
    
    @Override
    public String[] getFields() {
        return ReferenceItemFieldProvider.LOOTITEM_FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("quantity")) {
            return this.m_quantity;
        }
        return this.m_referenceItem.getFieldValue(fieldName);
    }
    
    public ReferenceItem getReferenceItem() {
        return this.m_referenceItem;
    }
    
    @Override
    public int compareTo(final ReferenceItemFieldProvider other) {
        if (other == null) {
            return -1;
        }
        final int comparisonByRarity = ItemRarity.getDescendingRarityComparator().compare(this.m_rarity, other.m_rarity);
        if (comparisonByRarity != 0) {
            return comparisonByRarity;
        }
        return Integer.signum(other.m_level - this.m_level);
    }
    
    static {
        LOOTITEM_FIELDS = new String[] { "quantity" };
    }
}
