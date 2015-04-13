package com.ankamagames.baseImpl.graphics.chat;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.graphics.image.*;

public class ChatFilterFieldProvider implements FieldProvider
{
    private final String m_name;
    private boolean m_enabled;
    private Color m_color;
    private final int m_id;
    public static final String NAME_FIELD = "name";
    public static final String ENABLED_FIELD = "enabled";
    public static final String COLOR_FIELD = "color";
    public final String[] FIELDS;
    
    public ChatFilterFieldProvider(final String name, final boolean enabled, final Color color, final int id) {
        super();
        this.FIELDS = new String[] { "name", "enabled", "color" };
        this.m_name = name;
        this.m_enabled = enabled;
        this.m_color = color;
        this.m_id = id;
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("enabled")) {
            return this.m_enabled;
        }
        if (fieldName.equals("color")) {
            return this.m_color;
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
    
    public Color getColor() {
        return this.m_color;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public int getId() {
        return this.m_id;
    }
}
