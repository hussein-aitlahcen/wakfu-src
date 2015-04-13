package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.util.xmlToJava.*;
import com.ankamagames.xulor2.core.*;

public class PrimitiveConverter implements Converter<Object>
{
    public static final Class<Object> TEMPLATE;
    
    @Override
    public Object convert(final String value) {
        return null;
    }
    
    @Override
    public Object convert(final Class<?> type, final String value) {
        return this.convert(type, value, null);
    }
    
    @Override
    public Object convert(final Class<?> type, final String value, final ElementMap map) {
        if (Boolean.TYPE.equals(type) || Boolean.class.equals(type)) {
            return com.ankamagames.framework.java.util.PrimitiveConverter.getBoolean(value);
        }
        if (Integer.TYPE.equals(type) || Integer.class.equals(type)) {
            return com.ankamagames.framework.java.util.PrimitiveConverter.getInteger(value);
        }
        if (Long.TYPE.equals(type) || Long.class.equals(type)) {
            return com.ankamagames.framework.java.util.PrimitiveConverter.getLong(value);
        }
        if (Float.TYPE.equals(type) || Float.class.equals(type)) {
            return com.ankamagames.framework.java.util.PrimitiveConverter.getFloat(value);
        }
        if (Double.TYPE.equals(type) || Double.class.equals(type)) {
            return com.ankamagames.framework.java.util.PrimitiveConverter.getDouble(value);
        }
        if (Byte.TYPE.equals(type) || Byte.class.equals(type)) {
            return com.ankamagames.framework.java.util.PrimitiveConverter.getByte(value);
        }
        if (Short.TYPE.equals(type) || Short.class.equals(type)) {
            return com.ankamagames.framework.java.util.PrimitiveConverter.getShort(value);
        }
        return null;
    }
    
    @Override
    public Class<Object> convertsTo() {
        return PrimitiveConverter.TEMPLATE;
    }
    
    public static Class<?> getClassFromPrimitive(final Class<?> primitive) {
        if (primitive.equals(Boolean.TYPE)) {
            return Boolean.class;
        }
        if (primitive.equals(Double.TYPE)) {
            return Double.class;
        }
        if (primitive.equals(Float.TYPE)) {
            return Float.class;
        }
        if (primitive.equals(Short.TYPE)) {
            return Short.class;
        }
        if (primitive.equals(Integer.TYPE)) {
            return Integer.class;
        }
        if (primitive.equals(Long.TYPE)) {
            return Long.class;
        }
        if (primitive.equals(Character.TYPE)) {
            return Character.class;
        }
        if (primitive.equals(Byte.TYPE)) {
            return Byte.class;
        }
        if (primitive.equals(Void.TYPE)) {
            return Void.class;
        }
        return null;
    }
    
    public static Class<?> getPrimitiveFromClass(final Class<?> type) {
        if (type.equals(Boolean.class)) {
            return Boolean.TYPE;
        }
        if (type.equals(Double.class)) {
            return Double.TYPE;
        }
        if (type.equals(Float.class)) {
            return Float.TYPE;
        }
        if (type.equals(Short.class)) {
            return Short.TYPE;
        }
        if (type.equals(Integer.class)) {
            return Integer.TYPE;
        }
        if (type.equals(Long.class)) {
            return Long.TYPE;
        }
        if (type.equals(Character.class)) {
            return Character.TYPE;
        }
        if (type.equals(Byte.class)) {
            return Byte.TYPE;
        }
        if (type.equals(Void.class)) {
            return Void.TYPE;
        }
        return null;
    }
    
    @Override
    public boolean canConvertFromScratch() {
        return true;
    }
    
    @Override
    public boolean canConvertWithoutVariables() {
        return true;
    }
    
    @Override
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<?> type, String attr, final Environment env) {
        final Object val = this.convert(type, attr);
        if (val instanceof Number) {
            attr = "(" + type.getName() + ")" + String.valueOf(val);
            if (type.equals(Float.class) || type.equals(Float.TYPE)) {
                attr += "f";
            }
        }
        else if (type.equals(Character.class)) {
            attr = "'" + attr + "'";
        }
        else if (val == null) {
            attr = "null";
        }
        else {
            attr = val.toString();
        }
        doc.addImport(type);
        return attr;
    }
    
    static {
        TEMPLATE = Object.class;
    }
}
