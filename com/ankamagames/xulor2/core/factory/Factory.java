package com.ankamagames.xulor2.core.factory;

import com.ankamagames.xulor2.util.xmlToJava.*;
import java.lang.reflect.*;

public interface Factory<T>
{
    public static final String SETTER_ID = "set";
    public static final String GETTER_ID = "get";
    public static final String PREPENDER_ID = "prepend";
    public static final String APPENDER_ID = "append";
    
    T newInstance(ClassDocument p0, String p1) throws Exception;
    
    T newInstance() throws Exception;
    
    T newInstance(Object p0) throws Exception;
    
    T newInstance(Object... p0) throws InstantiationException, IllegalAccessException, InvocationTargetException;
    
    Class<?> getTemplate();
    
    Method getSetter(Class<?> p0);
    
    Method getSetter(String p0);
    
    Method guessSetter(String p0);
    
    Method guessSetter(String p0, Class<?> p1);
    
    Method guessSetter(String p0, Class<?> p1, Class p2);
    
    Method guessGetter(String p0);
    
    Method guessGetter(String p0, Class<?> p1);
    
    Method guessPrepender(String p0);
    
    Method guessPrepender(String p0, Class<?> p1);
    
    Method guessAppender(String p0);
    
    Method guessAppender(String p0, Class<?> p1);
}
