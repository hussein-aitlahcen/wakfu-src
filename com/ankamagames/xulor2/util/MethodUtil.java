package com.ankamagames.xulor2.util;

import org.apache.log4j.*;
import java.lang.reflect.*;
import java.util.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.taglibrary.*;
import com.ankamagames.xulor2.core.factory.*;

public class MethodUtil
{
    private static Logger m_logger;
    
    public static boolean checkMethodParameter(final Method m, final Object value) {
        final Class[] types = m.getParameterTypes();
        if (types.length != 1) {
            return false;
        }
        final Class t = types[0];
        if (value == null) {
            return !t.isPrimitive();
        }
        final Class c = value.getClass();
        return (!t.isArray() || c.isArray()) && (t.isArray() || !c.isArray());
    }
    
    public static boolean checkMethodParameters(final Method m, final Object... values) {
        final Class[] types = m.getParameterTypes();
        if (values.length != types.length) {
            return false;
        }
        for (int i = 0; i < types.length; ++i) {
            if (values[i] == null) {
                if (types[i].isPrimitive()) {
                    return false;
                }
            }
            else if (!types[i].isAssignableFrom(values[i].getClass())) {
                return false;
            }
        }
        return true;
    }
    
    public static void castInvoke(final Method method, final Object invoker, final Object[] values) throws Exception {
        if (method == null || values == null) {
            return;
        }
        final Class<?>[] classes = method.getParameterTypes();
        if (classes.length != values.length) {
            throw new Exception("nombre de param\u00e8tres attendus : " + classes.length + ". Trouv\u00e9s : " + values.length);
        }
        final ArrayList<Object> parameters = new ArrayList<Object>();
        for (int i = 0; i < classes.length; ++i) {
            final Class<?> type = classes[i];
            final Object value = values[i];
            if ((value == null && !type.isPrimitive()) || (value != null && type.isAssignableFrom(value.getClass()))) {
                parameters.add(value);
            }
            else if (type.equals(String.class)) {
                parameters.add(PrimitiveConverter.getString(value));
            }
            else if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
                parameters.add(PrimitiveConverter.getBoolean(value));
            }
            else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
                parameters.add(PrimitiveConverter.getInteger(value));
            }
            else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
                parameters.add(PrimitiveConverter.getFloat(value));
            }
            else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
                parameters.add(PrimitiveConverter.getDouble(value));
            }
            else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
                parameters.add(PrimitiveConverter.getLong(value));
            }
            else {
                if (!value.getClass().equals(String.class)) {
                    throw new Exception("Impossible de convertir la valeur donn\u00e9e");
                }
                parameters.add(ConverterLibrary.getInstance().convert(type, (String)value));
            }
        }
        try {
            method.invoke(invoker, parameters.toArray());
        }
        catch (IllegalArgumentException e) {
            final StringBuilder sb = new StringBuilder();
            sb.append("IllegalArgumentException : method=").append(method).append(", parametres=");
            for (int j = 0; j < parameters.size(); ++j) {
                sb.append(parameters.get(j));
                if (j == parameters.size() - 1) {
                    break;
                }
                sb.append(", ");
            }
            MethodUtil.m_logger.error((Object)sb);
        }
    }
    
    public static void castInvokeWithItem(final String methodName, final EventDispatcher invoker, final Item item, final int attributeHash, final String field, final ResultProvider resultProvider) throws Exception {
        Object value = null;
        Object retValue = null;
        if ((retValue == null || resultProvider != null) && item != null) {
            if (field != null) {
                value = item.getFieldValue(field);
            }
            else {
                value = item.getValue();
            }
        }
        final ArrayList<Object> parameters = new ArrayList<Object>();
        if (resultProvider != null) {
            retValue = resultProvider.getResult(value);
        }
        final Object finalValue = (retValue != null) ? retValue : value;
        if (finalValue instanceof String && TextWidget.TEXT_HASH != attributeHash) {
            if (invoker.setXMLAttribute(attributeHash, (String)finalValue, ConverterLibrary.getInstance())) {
                return;
            }
        }
        else if (invoker.setPropertyAttribute(attributeHash, finalValue)) {
            return;
        }
        final Factory factory = XulorTagLibrary.getInstance().getFactory(invoker.getClass());
        Method method;
        if (retValue != null) {
            method = factory.guessSetter(methodName, retValue.getClass());
        }
        else if (value != null) {
            method = factory.guessSetter(methodName, value.getClass());
        }
        else {
            method = factory.guessSetter(methodName);
        }
        if (method == null) {
            return;
        }
        final Class<?>[] classes = method.getParameterTypes();
        if (classes.length != 1) {
            throw new Exception("La m\u00e9thode prend " + classes.length + " param\u00e8tres");
        }
        Class<?> type = classes[0];
        if (type.isPrimitive()) {
            type = com.ankamagames.xulor2.converter.PrimitiveConverter.getClassFromPrimitive(type);
        }
        if (retValue != null && type.isAssignableFrom(retValue.getClass())) {
            parameters.add(retValue);
        }
        else if (value == null || (value != null && type.isAssignableFrom(value.getClass()))) {
            parameters.add(value);
        }
        else if (type.equals(String.class)) {
            parameters.add(PrimitiveConverter.getString(value));
        }
        else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
            parameters.add(PrimitiveConverter.getBoolean(value));
        }
        else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
            parameters.add(PrimitiveConverter.getInteger(value));
        }
        else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
            parameters.add(PrimitiveConverter.getFloat(value));
        }
        else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
            parameters.add(PrimitiveConverter.getDouble(value));
        }
        else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
            parameters.add(PrimitiveConverter.getLong(value));
        }
        else {
            if (!value.getClass().equals(String.class)) {
                throw new Exception("Impossible de convertir la valeur donn\u00e9e (attendu = " + type + ", eu = " + value.getClass() + ")");
            }
            parameters.add(ConverterLibrary.getInstance().convert(type, (String)value));
        }
        try {
            method.invoke(invoker, parameters.toArray());
        }
        catch (IllegalArgumentException e) {
            final StringBuilder sb = new StringBuilder();
            sb.append("IllegalArgumentException : method=").append(method).append(", parametres=");
            for (int i = 0; i < parameters.size(); ++i) {
                sb.append(parameters.get(i));
                if (i == parameters.size() - 1) {
                    break;
                }
                sb.append(", ");
            }
            MethodUtil.m_logger.error((Object)sb);
        }
    }
    
    static {
        MethodUtil.m_logger = Logger.getLogger((Class)MethodUtil.class);
    }
}
