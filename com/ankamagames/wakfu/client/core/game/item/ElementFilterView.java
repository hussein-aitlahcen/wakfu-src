package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class ElementFilterView extends ImmutableFieldProvider
{
    public static final String ELEMENT_FIELD = "element";
    public static final String SELECTED_FIELD = "selected";
    private final ElementReference m_elementReference;
    private boolean m_selected;
    
    public ElementFilterView(final ElementReference elementReference) {
        super();
        this.m_elementReference = elementReference;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("element")) {
            return this.m_elementReference;
        }
        if (fieldName.equals("selected")) {
            return this.m_selected;
        }
        return null;
    }
    
    public void setSelected(final boolean selected) {
        this.m_selected = selected;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "selected");
    }
    
    public ElementReference getElementReference() {
        return this.m_elementReference;
    }
    
    public boolean isSelected() {
        return this.m_selected;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Elements) {
            return obj == this.m_elementReference.getElement();
        }
        return super.equals(obj);
    }
}
