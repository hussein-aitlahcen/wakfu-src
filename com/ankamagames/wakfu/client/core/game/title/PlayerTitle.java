package com.ankamagames.wakfu.client.core.game.title;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;

public class PlayerTitle implements FieldProvider
{
    public static final String ID_FIELD = "id";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String[] FIELDS;
    private short m_id;
    
    public PlayerTitle(final short id) {
        super();
        this.m_id = id;
    }
    
    public short getId() {
        return this.m_id;
    }
    
    public void setId(final short id) {
        this.m_id = id;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "id", "description");
    }
    
    private String getComboBoxDescription() {
        final String desc = this.getDescription();
        if (desc == null) {
            return WakfuTranslator.getInstance().getString("player.title.no.title");
        }
        return desc;
    }
    
    public String getDescription() {
        if (this.m_id == -1) {
            return null;
        }
        return WakfuTranslator.getInstance().getString(34, this.m_id, new Object[0]);
    }
    
    @Override
    public String[] getFields() {
        return PlayerTitle.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("id")) {
            return this.m_id;
        }
        if (fieldName.equals("description")) {
            return this.getComboBoxDescription();
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
        FIELDS = new String[] { "id", "description" };
    }
}
