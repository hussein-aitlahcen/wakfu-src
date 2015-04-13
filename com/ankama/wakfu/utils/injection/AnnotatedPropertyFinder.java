package com.ankama.wakfu.utils.injection;

import java.lang.annotation.*;
import org.apache.log4j.*;
import com.google.common.collect.*;
import java.util.*;
import java.lang.reflect.*;

class AnnotatedPropertyFinder
{
    private final Class<? extends Annotation> m_annotation;
    private static final Logger LOGGER;
    
    AnnotatedPropertyFinder(final Class<? extends Annotation> annotation) {
        super();
        this.m_annotation = annotation;
    }
    
    public Iterable<MethodDescriptor> getAnnotatedProperties(final Object target) {
        final Map<String, MethodDescriptor> annotatedPropertiesByName = (Map<String, MethodDescriptor>)Maps.newTreeMap();
        final Collection<Class<?>> classesToInspect = this.downwardClassHierarchy(target.getClass());
        for (final Class<?> classToInspect : classesToInspect) {
            annotatedPropertiesByName.putAll(this.getAnnotatedPropertiesOnClass(target, classToInspect));
        }
        return annotatedPropertiesByName.values();
    }
    
    private Collection<Class<?>> downwardClassHierarchy(final Class<?> theClass) {
        final LinkedList<Class<?>> downwardTypeHierarchy = (LinkedList<Class<?>>)Lists.newLinkedList();
        for (Class<?> declaringClass = theClass; declaringClass != Object.class; declaringClass = declaringClass.getSuperclass()) {
            downwardTypeHierarchy.addFirst(declaringClass);
        }
        return (Collection<Class<?>>)Lists.reverse((List)downwardTypeHierarchy);
    }
    
    private Map<String, MethodDescriptor> getAnnotatedPropertiesOnClass(final Object target, final Class<?> classToInspect) {
        final Map<String, MethodDescriptor> methodsInClass = (Map<String, MethodDescriptor>)Maps.newHashMap();
        for (final Method method : classToInspect.getDeclaredMethods()) {
            if (method.isAnnotationPresent(this.m_annotation)) {
                final MethodDescriptor descriptor = new MethodDescriptor(method, target);
                if (!descriptor.isProperty()) {
                    AnnotatedPropertyFinder.LOGGER.warn((Object)String.format("Method with @%s is not a property: %s", this.m_annotation.getSimpleName(), descriptor.getNotPropertyReason()));
                }
                else {
                    methodsInClass.put(method.getName(), descriptor);
                }
            }
        }
        return methodsInClass;
    }
    
    static {
        LOGGER = Logger.getLogger((Class)AnnotatedPropertyFinder.class);
    }
}
