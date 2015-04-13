package com.ankamagames.wakfu.client.core.landMarks;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.util.*;

public class LandMarkFilterElement extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String ID_FIELD = "id";
    public static final String SELECTED_FIELD = "selected";
    public static final String STYLE_FIELD = "style";
    public static final String[] FIELDS;
    private byte m_id;
    private boolean m_selected;
    
    public LandMarkFilterElement(final byte id) {
        super();
        this.m_id = id;
    }
    
    @Override
    public String[] getFields() {
        return LandMarkFilterElement.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("id")) {
            return this.m_id;
        }
        if (fieldName.equals("style")) {
            return "filter" + this.m_id;
        }
        if (fieldName.equals("selected")) {
            return this.m_selected;
        }
        return null;
    }
    
    private String getName() {
        return WakfuTranslator.getInstance().getString("landMark.type." + this.m_id);
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public void setId(final byte id) {
        if (id == this.m_id) {
            return;
        }
        this.m_id = id;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "name");
    }
    
    public boolean isSelected(final DisplayableMapPoint point) {
        return this.m_selected;
    }
    
    public void setSelected(final boolean selected) {
        if (selected == this.m_selected) {
            return;
        }
        this.m_selected = selected;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selected");
    }
    
    static {
        FIELDS = new String[] { "name", "id", "selected", "style" };
    }
}
