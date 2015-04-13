package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public class ItemTypeFilterFieldProvider implements FieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String ITEM_TYPE_FIELD = "itemType";
    public static final String SEARCH_SUB_CATEGORIES_FIELD = "searchSubCategories";
    public static final String THIS_FIELD = "this";
    public static final String ENABLED_FIELD = "enabled";
    protected ItemTypeFieldProvider m_selectedItemType;
    protected ItemType m_parentItemType;
    private ArrayList<ItemTypeFieldProvider> m_children;
    public final String[] FIELDS;
    
    public ItemTypeFilterFieldProvider(final ItemType itemType, final ItemType parentType) {
        super();
        this.FIELDS = new String[] { "name", "iconUrl", "itemType", "searchSubCategories", "this" };
        this.m_parentItemType = parentType;
        this.m_children = this.getChildrenItemTypes(itemType);
        this.m_selectedItemType = new ItemTypeFieldProvider(itemType);
        if (this.m_selectedItemType == null) {
            this.m_selectedItemType = new ItemTypeFieldProvider(null);
        }
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            final ItemType itemType = this.m_selectedItemType.getItemType();
            return (itemType == null) ? WakfuTranslator.getInstance().getString("all") : itemType.getName();
        }
        if (fieldName.equals("iconUrl")) {
            final ItemType itemType = this.m_selectedItemType.getItemType();
            String itemTypeIconUrl = WakfuConfiguration.getInstance().getItemTypeIconUrl((short)((itemType == null) ? -1 : itemType.getId()));
            if (itemTypeIconUrl == null) {
                itemTypeIconUrl = WakfuConfiguration.getInstance().getItemTypeIconUrl((short)(-1));
            }
            return itemTypeIconUrl;
        }
        if (fieldName.equals("searchSubCategories")) {
            return this.m_children;
        }
        if (fieldName.equals("this")) {
            return this;
        }
        if (fieldName.equals("enabled")) {
            return true;
        }
        if (fieldName.equals("itemType")) {
            return this.m_selectedItemType;
        }
        return null;
    }
    
    private ArrayList<ItemTypeFieldProvider> getChildrenItemTypes(final ItemType currentItemType) {
        GrowingArray<ItemType> childs = new GrowingArray<ItemType>();
        if (this.m_parentItemType == null) {
            for (final ItemType itemType : ItemTypeManager.getInstance().getRootTypes()) {
                childs.add(itemType);
            }
        }
        else {
            childs = this.m_parentItemType.getChilds();
        }
        final ArrayList<ItemTypeFieldProvider> itemTypes = new ArrayList<ItemTypeFieldProvider>();
        itemTypes.add(new ItemTypeFieldProvider(null));
        for (final ItemType itemType2 : childs) {
            if (itemType2.isVisibleInMarketPlace()) {
                itemTypes.add(new ItemTypeFieldProvider(itemType2));
            }
        }
        return itemTypes;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public ItemType getSelectedItemType() {
        return this.m_selectedItemType.getItemType();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ItemTypeFilterFieldProvider && this.getSelectedItemType() == ((ItemTypeFilterFieldProvider)obj).getSelectedItemType();
    }
}
