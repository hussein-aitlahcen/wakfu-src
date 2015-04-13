package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import java.util.*;

public class PackTypeFieldProvider implements FieldProvider
{
    public static final String QUANTITY_FIELD = "quantity";
    public static final String ENABLED_FIELD = "enabled";
    public static final String SELECTED_FIELD = "selected";
    private final PackType m_packType;
    public static final String[] FIELDS;
    private boolean m_enabled;
    private boolean m_selected;
    
    public PackTypeFieldProvider(final PackType packType, final boolean enabled, final boolean selected) {
        super();
        this.m_packType = packType;
        this.m_enabled = enabled;
        this.m_selected = selected;
    }
    
    public static Object[] toArray() {
        final ArrayList<String> list = new ArrayList<String>();
        for (final PackType type : PackType.values()) {
            list.add(getPackTypeRepresentation(type));
        }
        return list.toArray();
    }
    
    public static String getPackTypeRepresentation(final PackType packType) {
        return "x" + packType.qty;
    }
    
    public PackType getPackType() {
        return this.m_packType;
    }
    
    public short getQuantity() {
        return this.m_packType.qty;
    }
    
    public void setSelected(final boolean selected) {
        this.m_selected = selected;
    }
    
    @Override
    public String toString() {
        return "x" + this.getQuantity();
    }
    
    @Override
    public String[] getFields() {
        return PackTypeFieldProvider.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("quantity")) {
            return this.m_packType.qty;
        }
        if (fieldName.equals("enabled")) {
            return this.m_enabled;
        }
        if (fieldName.equals("selected")) {
            return this.m_selected;
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
    
    static {
        FIELDS = new String[] { "quantity" };
    }
}
