package com.ankama.wakfu.utils.injection;

import com.google.inject.*;
import java.util.*;
import com.google.common.collect.*;

public final class ModuleResolver implements IModuleResolver
{
    private final ClassToInstanceMap<Module> m_implementationMap;
    private final List<OverrideDeclaration> m_overrideDeclarations;
    private final ModuleDependencyAnnotationReader m_dependencyAnnotationReader;
    private final DependencyGraphMerger m_graphMerger;
    
    public static IModuleResolver get() {
        return new ModuleResolver();
    }
    
    @Override
    public Module resolveDependencies(final Module... modules) {
        final ModuleDependencyGraphBuilder builder = new ModuleDependencyGraphBuilder(this.m_dependencyAnnotationReader, (Iterable<Module>)ImmutableList.copyOf((Object[])modules), this.m_implementationMap, this.m_overrideDeclarations);
        final ModuleDependencyGraph graph = builder.buildGraph();
        return this.m_graphMerger.merge(graph);
    }
    
    @Override
    public <T extends Module> IModuleResolver usingImplementation(final Class<T> moduleInterface, final T implementationModule) {
        this.m_implementationMap.put((Object)moduleInterface, (Object)implementationModule);
        return this;
    }
    
    @Override
    public IModuleResolver addOverride(final Module... overridingModules) {
        this.m_overrideDeclarations.add(new OverrideDeclaration(Module.class, (Iterable<? extends Module>)ImmutableList.copyOf((Object[])overridingModules)));
        return this;
    }
    
    @Override
    public IModuleResolver addOverride(final Class<? extends Module> overridenModuleType, final Module... overridingModules) {
        this.m_overrideDeclarations.add(new OverrideDeclaration(overridenModuleType, (Iterable<? extends Module>)ImmutableList.copyOf((Object[])overridingModules)));
        return this;
    }
    
    @Override
    public IModuleResolver setOverride(final Class<? extends Module> overridenModuleType, final Module... overridingModules) {
        final Iterator<OverrideDeclaration> iterator = this.m_overrideDeclarations.iterator();
        while (iterator.hasNext()) {
            final OverrideDeclaration overrideDeclaration = iterator.next();
            if (overrideDeclaration.getOverridenModuleType() == overridenModuleType) {
                iterator.remove();
            }
        }
        this.addOverride(overridenModuleType, overridingModules);
        return this;
    }
    
    private ModuleResolver() {
        super();
        this.m_implementationMap = (ClassToInstanceMap<Module>)MutableClassToInstanceMap.create();
        this.m_overrideDeclarations = (List<OverrideDeclaration>)Lists.newArrayList();
        this.m_dependencyAnnotationReader = new ModuleDependencyAnnotationReader();
        this.m_graphMerger = new DependencyGraphMerger();
    }
}
