package com.ankamagames.wakfu.client.core.game.item;

import java.util.*;

public class StackInventoryView extends AbstractBagView
{
    private final String m_name;
    private ArrayList<QuestItemView> m_items;
    
    public StackInventoryView(final String name) {
        super();
        this.m_items = new ArrayList<QuestItemView>();
        this.m_name = name;
    }
    
    @Override
    public String[] getFields() {
        return StackInventoryView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("bagNameSize")) {
            return this.m_name;
        }
        if (fieldName.equals("bagInventory")) {
            return this.m_items;
        }
        if (fieldName.equals("canBeSorted")) {
            return false;
        }
        return null;
    }
    
    public void setItems(final ArrayList<QuestItemView> items) {
        this.m_items = items;
    }
}
