package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.xulor2.core.*;

public class StringCondition extends UnaryConditionOperator
{
    public static final String TAG = "StringCondition";
    
    @Override
    public String getTag() {
        return "StringCondition";
    }
    
    @Override
    public boolean isValid(Object value) {
        if (this.m_comparedValueInit) {
            value = this.m_comparedValue;
        }
        if (!(value instanceof String) || this.m_key == null) {
            return false;
        }
        final String string = (String)value;
        if (this.m_key.equalsIgnoreCase("length")) {
            return this.m_condition != null && this.m_condition.isValid(string.length());
        }
        if (this.m_key.equalsIgnoreCase("startsWith")) {
            if (!(this.m_value instanceof String)) {
                return false;
            }
            final boolean ok = string.startsWith((String)this.m_value);
            return (this.m_condition == null) ? ok : this.m_condition.isValid(ok);
        }
        else {
            if (!this.m_key.equalsIgnoreCase("endsWith")) {
                return false;
            }
            if (!(this.m_value instanceof String)) {
                return false;
            }
            final boolean ok = string.endsWith((String)this.m_value);
            return (this.m_condition == null) ? ok : this.m_condition.isValid(ok);
        }
    }
    
    public Condition cloneCondition() {
        final StringCondition clone = new StringCondition();
        this.copyElement(clone);
        return clone;
    }
}
