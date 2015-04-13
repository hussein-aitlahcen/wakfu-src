package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.framework.java.util.*;

public class LessCondition extends Condition
{
    public static final String TAG = "isLess";
    
    @Override
    public String getTag() {
        return "isLess";
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        if (object instanceof Integer) {
            return PrimitiveConverter.getInteger(object) < PrimitiveConverter.getInteger(this.m_value);
        }
        if (object instanceof Float) {
            return PrimitiveConverter.getFloat(object) < PrimitiveConverter.getFloat(this.m_value);
        }
        if (object instanceof Double) {
            return PrimitiveConverter.getDouble(object) < PrimitiveConverter.getDouble(this.m_value);
        }
        if (object instanceof Short) {
            return PrimitiveConverter.getShort(object) < PrimitiveConverter.getShort(this.m_value);
        }
        if (object instanceof Long) {
            return PrimitiveConverter.getLong(object) < PrimitiveConverter.getLong(this.m_value);
        }
        if (object instanceof Byte) {
            return PrimitiveConverter.getByte(object) < PrimitiveConverter.getByte(this.m_value);
        }
        return object instanceof String && PrimitiveConverter.getDouble(object) < PrimitiveConverter.getDouble(this.m_value);
    }
}
