package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.converter.*;

public class ItemCondition extends UnaryConditionOperator
{
    public static final String TAG = "ItemCondition";
    public static final int FIELD_HASH;
    
    @Override
    public String getTag() {
        return "ItemCondition";
    }
    
    @Override
    public boolean isValid(Object value) {
        if (this.m_comparedValueInit) {
            value = this.m_comparedValue;
        }
        if (!(value instanceof RenderableContainer) && !(value instanceof FieldProvider)) {
            return false;
        }
        if (value instanceof FieldProvider) {
            if (this.m_key != null) {
                return this.m_condition.isValid(((FieldProvider)value).getFieldValue(this.m_key));
            }
            return this.m_condition.isValid(value);
        }
        else {
            final RenderableContainer renderable = (RenderableContainer)value;
            final Item item = renderable.getItem();
            if (item != null && item.getValue() instanceof FieldProvider && this.m_key != null) {
                return this.m_condition.isValid(item.getFieldValue(this.m_key));
            }
            final Object returnedValue = (item == null) ? null : item.getValue();
            return this.m_condition.isValid(returnedValue);
        }
    }
    
    public void setField(final String field) {
        this.setKey(field);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ItemCondition.FIELD_HASH) {
            this.setField(cl.convertToString(value, this.m_elementMap));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ItemCondition.FIELD_HASH) {
            this.setField(String.valueOf(value));
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        FIELD_HASH = "field".hashCode();
    }
}
