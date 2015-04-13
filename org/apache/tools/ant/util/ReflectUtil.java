package org.apache.tools.ant.util;

import org.apache.tools.ant.*;
import java.lang.reflect.*;

public class ReflectUtil
{
    public static <T> T newInstance(final Class<T> ofClass, final Class<?>[] argTypes, final Object[] args) {
        try {
            final Constructor<T> con = ofClass.getConstructor(argTypes);
            return con.newInstance(args);
        }
        catch (Exception t) {
            throwBuildException(t);
            return null;
        }
    }
    
    public static Object invoke(final Object obj, final String methodName) {
        try {
            final Method method = obj.getClass().getMethod(methodName, (Class<?>[])null);
            return method.invoke(obj, (Object[])null);
        }
        catch (Exception t) {
            throwBuildException(t);
            return null;
        }
    }
    
    public static Object invokeStatic(final Object obj, final String methodName) {
        try {
            final Method method = ((Class)obj).getMethod(methodName, (Class[])null);
            return method.invoke(obj, (Object[])null);
        }
        catch (Exception t) {
            throwBuildException(t);
            return null;
        }
    }
    
    public static Object invoke(final Object obj, final String methodName, final Class<?> argType, final Object arg) {
        try {
            final Method method = obj.getClass().getMethod(methodName, argType);
            return method.invoke(obj, arg);
        }
        catch (Exception t) {
            throwBuildException(t);
            return null;
        }
    }
    
    public static Object invoke(final Object obj, final String methodName, final Class<?> argType1, final Object arg1, final Class<?> argType2, final Object arg2) {
        try {
            final Method method = obj.getClass().getMethod(methodName, argType1, argType2);
            return method.invoke(obj, arg1, arg2);
        }
        catch (Exception t) {
            throwBuildException(t);
            return null;
        }
    }
    
    public static Object getField(final Object obj, final String fieldName) throws BuildException {
        try {
            final Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        }
        catch (Exception t) {
            throwBuildException(t);
            return null;
        }
    }
    
    public static void throwBuildException(final Exception t) throws BuildException {
        throw toBuildException(t);
    }
    
    public static BuildException toBuildException(final Exception t) {
        if (!(t instanceof InvocationTargetException)) {
            return new BuildException(t);
        }
        final Throwable t2 = ((InvocationTargetException)t).getTargetException();
        if (t2 instanceof BuildException) {
            return (BuildException)t2;
        }
        return new BuildException(t2);
    }
    
    public static boolean respondsTo(final Object o, final String methodName) throws BuildException {
        try {
            final Method[] methods = o.getClass().getMethods();
            for (int i = 0; i < methods.length; ++i) {
                if (methods[i].getName().equals(methodName)) {
                    return true;
                }
            }
            return false;
        }
        catch (Exception t) {
            throw toBuildException(t);
        }
    }
}
