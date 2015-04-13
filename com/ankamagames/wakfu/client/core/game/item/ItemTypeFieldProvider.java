package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;

public class ItemTypeFieldProvider implements FieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String ITEM_TYPE_FIELD = "itemType";
    protected ItemType m_itemType;
    public final String[] FIELDS;
    
    public ItemTypeFieldProvider(final ItemType itemType) {
        super();
        this.FIELDS = new String[] { "name", "iconUrl", "itemType" };
        this.m_itemType = itemType;
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return (this.m_itemType == null) ? WakfuTranslator.getInstance().getString("all") : this.m_itemType.getName();
        }
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getItemTypeIconUrl((short)((this.m_itemType == null) ? -1 : this.m_itemType.getId()));
        }
        if (fieldName.equals("itemType")) {
            return this.m_itemType;
        }
        return null;
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
    
    public ItemType getItemType() {
        return this.m_itemType;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ItemTypeFieldProvider && this.m_itemType == ((ItemTypeFieldProvider)obj).getItemType();
    }
}
