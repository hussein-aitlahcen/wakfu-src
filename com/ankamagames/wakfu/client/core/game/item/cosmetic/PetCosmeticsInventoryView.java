package com.ankamagames.wakfu.client.core.game.item.cosmetic;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics.*;

public class PetCosmeticsInventoryView extends ImmutableFieldProvider implements CosmeticsInventoryListener
{
    public static final String ITEMS = "items";
    public static final String FILTERED_ITEMS = "filteredItems";
    public static final String SELECTED = "selectedItem";
    static final int NULL_REF_ID = 0;
    private final ArrayList<PetCosmeticsItemView> m_items;
    private final ArrayList<PetCosmeticsItemView> m_filteredItems;
    private PetCosmeticsItemView m_selectedItem;
    private static Comparator<PetCosmeticsItemView> COMPARATOR;
    private PetDefinition m_filter;
    
    public PetCosmeticsInventoryView() {
        super();
        this.m_items = new ArrayList<PetCosmeticsItemView>();
        this.m_filteredItems = new ArrayList<PetCosmeticsItemView>();
        this.m_items.add(new PetCosmeticsItemView(0));
    }
    
    public void filter(final PetDefinition def) {
        this.m_filter = def;
        this.filter();
    }
    
    private void filter() {
        if (this.m_filter == null) {
            return;
        }
        this.m_filteredItems.clear();
        for (int i = 0, size = this.m_items.size(); i < size; ++i) {
            final PetCosmeticsItemView itemView = this.m_items.get(i);
            if (itemView.getRefId() == 0 || this.m_filter.containsEquipment(itemView.getRefId()) || this.m_filter.containsReskinItem(itemView.getRefId())) {
                this.m_filteredItems.add(itemView);
            }
        }
        Collections.sort(this.m_filteredItems, PetCosmeticsInventoryView.COMPARATOR);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "filteredItems");
    }
    
    @Override
    public String[] getFields() {
        return PetCosmeticsInventoryView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("items")) {
            return this.m_items;
        }
        if (fieldName.equals("filteredItems")) {
            return this.m_filteredItems;
        }
        if (fieldName.equals("selectedItem")) {
            return this.m_selectedItem;
        }
        return null;
    }
    
    public void select(final Item item) {
        this.select((item == null) ? 0 : item.getReferenceId());
    }
    
    public void select(final int itemRefId) {
        this.m_selectedItem = null;
        for (int i = 0, size = this.m_items.size(); i < size; ++i) {
            final PetCosmeticsItemView itemView = this.m_items.get(i);
            if (itemView.getRefId() == itemRefId) {
                this.m_selectedItem = itemView;
                break;
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selectedItem");
    }
    
    @Override
    public void itemAdded(final CosmeticsItem item) {
        this.m_items.add(new PetCosmeticsItemView(item.getRefId()));
        Collections.sort(this.m_items, PetCosmeticsInventoryView.COMPARATOR);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "items");
        this.filter();
    }
    
    @Override
    public void itemRemoved(final int itemId) {
        for (int i = 0, size = this.m_items.size(); i < size; ++i) {
            final PetCosmeticsItemView itemView = this.m_items.get(i);
            if (itemView.getRefId() == itemId) {
                this.m_items.remove(i);
                break;
            }
        }
        Collections.sort(this.m_items, PetCosmeticsInventoryView.COMPARATOR);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "items");
        this.filter();
    }
    
    static {
        PetCosmeticsInventoryView.COMPARATOR = new Comparator<PetCosmeticsItemView>() {
            @Override
            public int compare(final PetCosmeticsItemView o1, final PetCosmeticsItemView o2) {
                return o1.getRefId() - o2.getRefId();
            }
        };
    }
}
