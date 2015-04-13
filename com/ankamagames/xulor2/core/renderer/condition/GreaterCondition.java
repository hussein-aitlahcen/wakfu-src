package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.framework.java.util.*;

public class GreaterCondition extends Condition
{
    public static final String TAG = "isGreater";
    
    @Override
    public String getTag() {
        return "isGreater";
    }
    
    @Override
    public boolean isValid(final Object object) {
        final Object compared = this.m_comparedValueInit ? this.m_comparedValue : object;
        if (compared instanceof Integer) {
            return PrimitiveConverter.getInteger(compared) > PrimitiveConverter.getInteger(this.m_value);
        }
        if (compared instanceof Float) {
            return PrimitiveConverter.getFloat(compared) > PrimitiveConverter.getFloat(this.m_value);
        }
        if (compared instanceof Double) {
            return PrimitiveConverter.getDouble(compared) > PrimitiveConverter.getDouble(this.m_value);
        }
        if (compared instanceof Short) {
            return PrimitiveConverter.getShort(compared) > PrimitiveConverter.getShort(this.m_value);
        }
        if (compared instanceof Long) {
            return PrimitiveConverter.getLong(compared) > PrimitiveConverter.getLong(this.m_value);
        }
        if (compared instanceof Byte) {
            return PrimitiveConverter.getByte(compared) > PrimitiveConverter.getByte(this.m_value);
        }
        return compared instanceof String && PrimitiveConverter.getDouble(compared) > PrimitiveConverter.getDouble(this.m_value);
    }
}
