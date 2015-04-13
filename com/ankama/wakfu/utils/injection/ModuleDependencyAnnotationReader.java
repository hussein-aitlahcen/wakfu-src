package com.ankama.wakfu.utils.injection;

import org.apache.log4j.*;
import java.lang.annotation.*;
import com.google.inject.*;
import com.google.common.base.*;
import com.google.common.collect.*;
import java.util.*;

class ModuleDependencyAnnotationReader
{
    private final AnnotatedPropertyFinder m_requiredModulesPropertyFinder;
    private final AnnotatedPropertyFinder m_requiredModuleTypesPropertyFinder;
    private static final Logger LOGGER;
    
    ModuleDependencyAnnotationReader() {
        super();
        this.m_requiredModulesPropertyFinder = new AnnotatedPropertyFinder(RequiredModules.class);
        this.m_requiredModuleTypesPropertyFinder = new AnnotatedPropertyFinder(RequiredModuleTypes.class);
    }
    
    public Iterable<Module> getRequiredModules(final Module module) {
        final Iterable<MethodDescriptor> requiredModulesMethods = this.m_requiredModulesPropertyFinder.getAnnotatedProperties(module);
        return this.concatAllProperties(requiredModulesMethods, Module.class);
    }
    
    public Iterable<Class<? extends Module>> getRequiredModuleTypes(final Module module) {
        final Iterable<MethodDescriptor> requiredModulesMethods = this.m_requiredModuleTypesPropertyFinder.getAnnotatedProperties(module);
        final Iterable<Class> requiredTypes = (Iterable<Class>)this.concatAllProperties(requiredModulesMethods, Class.class);
        return this.filterModuleInterfaces(requiredTypes);
    }
    
    private <T> Iterable<T> concatAllProperties(final Iterable<MethodDescriptor> properties, final Class<T> itemType) {
        final VariableSizePropertyInvoker<T> invoker = new VariableSizePropertyInvoker<T>(itemType);
        final Function<MethodDescriptor, Iterable<T>> toModules = (Function<MethodDescriptor, Iterable<T>>)new Function<MethodDescriptor, Iterable<T>>() {
            public Iterable<T> apply(final MethodDescriptor method) {
                return invoker.invoke(method);
            }
        };
        return (Iterable<T>)Lists.newArrayList(Iterables.concat(Iterables.transform((Iterable)properties, (Function)toModules)));
    }
    
    private Collection<Class<? extends Module>> filterModuleInterfaces(final Iterable<Class> requiredTypes) {
        final Collection<Class<? extends Module>> requiredModuleTypes = (Collection<Class<? extends Module>>)Lists.newArrayList();
        for (final Class<?> requiredType : requiredTypes) {
            if (!this.isModuleType(requiredType)) {
                continue;
            }
            requiredModuleTypes.add(this.toModuleType(requiredType));
        }
        return requiredModuleTypes;
    }
    
    private boolean isModuleType(final Class<?> requiredType) {
        if (!requiredType.isInterface()) {
            ModuleDependencyAnnotationReader.LOGGER.warn((Object)String.format("Required module type %s is not an interface.", requiredType.getSimpleName()));
            return false;
        }
        if (!Module.class.isAssignableFrom(requiredType)) {
            ModuleDependencyAnnotationReader.LOGGER.warn((Object)String.format("Required module type %s does not extend Module.", requiredType.getSimpleName()));
            return false;
        }
        return true;
    }
    
    private Class<? extends Module> toModuleType(final Class<?> requiredType) {
        return (Class<? extends Module>)requiredType;
    }
    
    static {
        LOGGER = Logger.getLogger((Class)ModuleDependencyAnnotationReader.class);
    }
}
