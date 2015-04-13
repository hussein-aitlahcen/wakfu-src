package com.ankama.wakfu.utils.injection;

import org.apache.log4j.*;
import com.google.common.base.*;
import com.google.common.collect.*;
import java.lang.reflect.*;
import java.util.*;

class VariableSizePropertyInvoker<T>
{
    private final Class<T> m_propertyElementType;
    private static final Logger LOGGER;
    
    VariableSizePropertyInvoker(final Class<T> propertyElementType) {
        super();
        this.m_propertyElementType = propertyElementType;
    }
    
    public Iterable<T> invoke(final MethodDescriptor method) {
        Preconditions.checkArgument(method.isProperty(), (Object)"'method' must be a property.");
        if (this.methodReturnsSingleItem(method)) {
            return this.getSingleItem(method);
        }
        if (this.methodReturnsIterable(method)) {
            return this.getFilteredIterable(method);
        }
        if (this.methodReturnsItemArray(method)) {
            return this.getItemArray(method);
        }
        this.logInvalidMethodType(method);
        return (Iterable<T>)Collections.emptyList();
    }
    
    private boolean methodReturnsSingleItem(final MethodDescriptor method) {
        return this.m_propertyElementType.isAssignableFrom(method.getReturnType());
    }
    
    private Iterable<T> getSingleItem(final MethodDescriptor method) {
        return Collections.singleton(this.m_propertyElementType.cast(method.invokeAsProperty()));
    }
    
    private boolean methodReturnsIterable(final MethodDescriptor method) {
        return Iterable.class.isAssignableFrom(method.getReturnType());
    }
    
    private Iterable<T> getFilteredIterable(final MethodDescriptor method) {
        return (Iterable<T>)Iterables.filter((Iterable)method.invokeAsProperty(), (Class)this.m_propertyElementType);
    }
    
    private boolean methodReturnsItemArray(final MethodDescriptor method) {
        final Class<?> itemArrayType = Array.newInstance(this.m_propertyElementType, 0).getClass();
        return itemArrayType.equals(method.getReturnType());
    }
    
    private List<T> getItemArray(final MethodDescriptor method) {
        return Arrays.asList((T[])method.invokeAsProperty());
    }
    
    private void logInvalidMethodType(final MethodDescriptor method) {
        VariableSizePropertyInvoker.LOGGER.warn((Object)String.format("The method %s must have a return type of %s, Iterable, or %s[]", method.toString(), this.m_propertyElementType.getSimpleName(), this.m_propertyElementType.getSimpleName()));
    }
    
    static {
        LOGGER = Logger.getLogger((Class)VariableSizePropertyInvoker.class);
    }
}
