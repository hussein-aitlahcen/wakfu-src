package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.framework.java.util.*;

public class BitwiseOperation extends UnaryConditionOperator
{
    public static final String TAG = "BitwiseOperation";
    public static final String AND_KEY = "and";
    public static final String OR_KEY = "or";
    public static final String NOT_KEY = "not";
    
    @Override
    public String getTag() {
        return "BitwiseOperation";
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        Object value = null;
        if (this.m_key != null) {
            if (this.m_key.equalsIgnoreCase("and")) {
                if (object instanceof Integer) {
                    value = (PrimitiveConverter.getInteger(object) & PrimitiveConverter.getInteger(this.m_value));
                }
                else if (object instanceof Short) {
                    value = (PrimitiveConverter.getShort(object) & PrimitiveConverter.getShort(this.m_value));
                }
                else if (object instanceof Long) {
                    value = (PrimitiveConverter.getLong(object) & PrimitiveConverter.getLong(this.m_value));
                }
            }
            if (this.m_key.equalsIgnoreCase("or")) {
                if (object instanceof Integer) {
                    value = (PrimitiveConverter.getInteger(object) | PrimitiveConverter.getInteger(this.m_value));
                }
                else if (object instanceof Short) {
                    value = (PrimitiveConverter.getShort(object) | PrimitiveConverter.getShort(this.m_value));
                }
                else if (object instanceof Long) {
                    value = (PrimitiveConverter.getLong(object) | PrimitiveConverter.getLong(this.m_value));
                }
            }
        }
        return this.m_condition.isValid(value);
    }
}
