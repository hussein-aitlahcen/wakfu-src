package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;

public abstract class Condition extends NonGraphicalElement
{
    protected String m_key;
    protected Object m_value;
    protected Object m_comparedValue;
    protected boolean m_comparedValueInit;
    protected ConditionResult m_conditionParent;
    public static final boolean DEBUG_CONDITIONS = false;
    public static final int VALUE_HASH;
    public static final int COMPARED_VALUE_HASH;
    public static final int KEY_HASH;
    
    public Condition() {
        super();
        this.m_key = null;
        this.m_value = null;
        this.m_comparedValue = null;
        this.m_comparedValueInit = false;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public void setValue(final Object value) {
        if ((value != null && !value.equals(this.m_value)) || (this.m_value != null && !this.m_value.equals(value))) {
            this.m_value = value;
            this.fireConditionChanged(true);
        }
    }
    
    public Object getComparedValue() {
        return this.m_comparedValue;
    }
    
    public void setComparedValue(final Object value) {
        if ((value != null && !value.equals(this.m_comparedValue)) || (this.m_comparedValue != null && !this.m_comparedValue.equals(value)) || (value == null && this.m_comparedValue == null)) {
            this.m_comparedValue = value;
            this.fireConditionChanged(this.m_comparedValueInit = true);
        }
    }
    
    public void fireConditionChanged(final boolean full) {
        if (this.m_parent instanceof Condition) {
            ((Condition)this.m_parent).fireConditionChanged(full);
        }
        else if (this.m_parent instanceof ConditionResult) {
            ((ConditionResult)this.m_parent).fireConditionChanged(full);
        }
    }
    
    public String getKey() {
        return this.m_key;
    }
    
    public void setKey(final String key) {
        this.m_key = key;
    }
    
    public ConditionResult getConditionParent() {
        return this.m_conditionParent;
    }
    
    public void setConditionParent(final ConditionResult parent) {
        this.m_conditionParent = parent;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final Condition condition = (Condition)source;
        condition.setKey(this.m_key);
        condition.setValue(this.m_value);
        if (this.m_comparedValueInit) {
            condition.setComparedValue(this.m_comparedValue);
        }
        super.copyElement(condition);
    }
    
    public abstract boolean isValid(final Object p0);
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Condition.COMPARED_VALUE_HASH) {
            this.setComparedValue(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == Condition.VALUE_HASH) {
            this.setValue(cl.convertToString(value, this.m_elementMap));
        }
        else {
            if (hash != Condition.KEY_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setKey(cl.convertToString(value, this.m_elementMap));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Condition.COMPARED_VALUE_HASH) {
            this.setComparedValue(value);
        }
        else if (hash == Condition.VALUE_HASH) {
            this.setValue(value);
        }
        else {
            if (hash != Condition.KEY_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setKey(String.valueOf(value));
        }
        return true;
    }
    
    static {
        VALUE_HASH = "value".hashCode();
        COMPARED_VALUE_HASH = "comparedValue".hashCode();
        KEY_HASH = "key".hashCode();
    }
}
