package com.ankama.wakfu.utils.injection;

import com.ankama.wakfu.utils.*;
import com.google.common.base.*;
import java.lang.reflect.*;

class MethodDescriptor
{
    private final Method m_method;
    private final Object m_receiver;
    private final Either<String, Void> m_propertyStatus;
    
    MethodDescriptor(final Method method, final Object receiver) {
        super();
        this.m_method = method;
        this.m_receiver = receiver;
        this.m_propertyStatus = this.propertyStatus();
    }
    
    public Class<?> getReturnType() {
        return this.m_method.getReturnType();
    }
    
    public boolean isProperty() {
        return this.m_propertyStatus.isValid();
    }
    
    public String getNotPropertyReason() {
        if (this.isProperty()) {
            throw new IllegalStateException(String.format("Method %s is a property", this));
        }
        return this.m_propertyStatus.getError();
    }
    
    public Object invokeAsProperty() {
        final boolean accessibleFlag = this.m_method.isAccessible();
        try {
            this.m_method.setAccessible(true);
            return this.tryInvokeAsProperty();
        }
        catch (IllegalAccessException e) {
            throw Throwables.propagate((Throwable)e);
        }
        catch (InvocationTargetException e2) {
            throw Throwables.propagate((Throwable)e2);
        }
        finally {
            this.m_method.setAccessible(accessibleFlag);
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s.%s", this.m_method.getDeclaringClass().getSimpleName(), this.m_method.getName());
    }
    
    private Either<String, Void> propertyStatus() {
        if (this.m_method.getParameterTypes().length > 0) {
            return Either.error(String.format("Method %s has more than 0 parameters.", this));
        }
        return Either.valid((Void)null);
    }
    
    private Object tryInvokeAsProperty() throws IllegalAccessException, InvocationTargetException {
        if (!this.isProperty()) {
            throw new IllegalStateException(String.format("Method %s is not a property.", this));
        }
        return this.m_method.invoke(this.m_receiver, new Object[0]);
    }
}
