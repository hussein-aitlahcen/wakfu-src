package com.ankama.wakfu.utils.injection;

import com.google.inject.*;
import java.util.*;
import com.google.common.base.*;
import com.google.common.collect.*;

class ModuleDependencyGraphBuilder
{
    private final ModuleDependencyAnnotationReader m_dependencyAnnotationReader;
    private final ClassToInstanceMap<Module> m_implementationMap;
    private final Iterable<Module> m_initialModules;
    private final Set<DependencyNode> m_nodes;
    private final Set<DependencyNode> m_resolvedNodes;
    private final Map<OverrideDeclaration, DependencyLink> m_overrideLinks;
    private final List<OverrideDeclaration> m_overrides;
    
    public ModuleDependencyGraphBuilder(final ModuleDependencyAnnotationReader dependencyAnnotationReader, final Iterable<Module> initialModules, final ClassToInstanceMap<Module> implementationMap, final List<OverrideDeclaration> overrides) {
        super();
        this.m_nodes = (Set<DependencyNode>)Sets.newLinkedHashSet();
        this.m_resolvedNodes = (Set<DependencyNode>)Sets.newHashSet();
        this.m_overrideLinks = (Map<OverrideDeclaration, DependencyLink>)Maps.newLinkedHashMap();
        this.m_dependencyAnnotationReader = dependencyAnnotationReader;
        this.m_initialModules = initialModules;
        this.m_implementationMap = implementationMap;
        this.m_overrides = overrides;
    }
    
    public ModuleDependencyGraph buildGraph() {
        final DependencyNode root = new CombinedDependencyNode();
        this.m_nodes.add(root);
        for (final Module initialModule : this.m_initialModules) {
            this.addModuleLink(root, initialModule);
        }
        for (final OverrideDeclaration unconditionalOverride : this.overridesForModuleType(Module.class)) {
            this.addOverrideList(root, unconditionalOverride);
        }
        this.m_resolvedNodes.add(root);
        DependencyNode next;
        while ((next = (DependencyNode)Iterables.getFirst((Iterable)this.getUnresolvedNodes(), (Object)null)) != null) {
            this.resolveDependencies(next);
        }
        return new ModuleDependencyGraph(this.m_nodes, root, Iterables.filter((Iterable)Lists.transform((List)this.m_overrides, Functions.forMap((Map)this.m_overrideLinks, (Object)null)), Predicates.notNull()));
    }
    
    private List<OverrideDeclaration> overridesForModuleType(final Class<? extends Module> moduleType) {
        return (List<OverrideDeclaration>)FluentIterable.from((Iterable)this.m_overrides).filter((Predicate)new Predicate<OverrideDeclaration>() {
            public boolean apply(final OverrideDeclaration input) {
                return input.getOverridenModuleType() == moduleType;
            }
        }).toList();
    }
    
    private void addModuleLink(final DependencyNode source, final Module target) {
        final ModuleDependencyNode node = new ModuleDependencyNode(target);
        this.addRegularNodeLink(source, node);
    }
    
    private void addRegularNodeLink(final DependencyNode source, final DependencyNode node) {
        this.addLink(DependencyLink.Type.REGULAR, source, node);
    }
    
    private DependencyLink addLink(final DependencyLink.Type type, final DependencyNode source, final DependencyNode target) {
        Preconditions.checkArgument(this.m_nodes.contains(source), (Object)"Cannot add a link if source is not in graph.");
        this.m_nodes.add(target);
        final DependencyLink link = new DependencyLink(type, source, (DependencyNode)Iterables.find((Iterable)this.m_nodes, Predicates.equalTo((Object)target)));
        source.addLink(link);
        return link;
    }
    
    private Set<DependencyNode> getUnresolvedNodes() {
        return (Set<DependencyNode>)Sets.difference((Set)this.m_nodes, (Set)this.m_resolvedNodes);
    }
    
    private void resolveDependencies(final DependencyNode node) {
        if (node instanceof ModuleInterfaceDependencyNode) {
            this.resolveInterfaceDependencies((ModuleInterfaceDependencyNode)node);
        }
        else if (node instanceof ModuleDependencyNode) {
            this.resolveModuleDependencies((ModuleDependencyNode)node);
        }
    }
    
    private void resolveModuleDependencies(final ModuleDependencyNode node) {
        final Module module = node.getModule();
        this.addModuleDependencies(node, module);
        this.addModuleTypeDependencies(node, module);
        this.addModuleOverrideLinks(node, module);
        this.m_resolvedNodes.add(node);
    }
    
    private void addModuleDependencies(final ModuleDependencyNode node, final Module module) {
        final Iterable<Module> requiredModules = this.m_dependencyAnnotationReader.getRequiredModules(module);
        for (final Module requiredModule : requiredModules) {
            this.addModuleLink(node, requiredModule);
        }
    }
    
    private void addModuleTypeDependencies(final ModuleDependencyNode node, final Module module) {
        final Iterable<Class<? extends Module>> requiredModuleTypes = this.m_dependencyAnnotationReader.getRequiredModuleTypes(module);
        for (final Class<? extends Module> requiredModuleType : requiredModuleTypes) {
            this.addRegularNodeLink(node, new ModuleInterfaceDependencyNode(requiredModuleType));
        }
    }
    
    private void addModuleOverrideLinks(final ModuleDependencyNode node, final Module module) {
        final List<OverrideDeclaration> overrides = this.overridesForModuleType(module.getClass());
        if (overrides.isEmpty()) {
            return;
        }
        for (final OverrideDeclaration overrideDeclaration : overrides) {
            this.addOverrideList(node, overrideDeclaration);
        }
    }
    
    private void resolveInterfaceDependencies(final ModuleInterfaceDependencyNode node) {
        final Class<? extends Module> moduleInterface = node.getModuleInterface();
        final Module implementationModule = (Module)this.m_implementationMap.get((Object)moduleInterface);
        if (implementationModule == null) {
            throw new IllegalArgumentException("No implementation configured for " + moduleInterface.getName());
        }
        this.addModuleLink(node, implementationModule);
        this.m_resolvedNodes.add(node);
    }
    
    private void addOverrideList(final DependencyNode node, final OverrideDeclaration overrideDeclaration) {
        final ImmutableList<Module> overrideList = overrideDeclaration.getOverridingModules();
        if (overrideList.isEmpty()) {
            return;
        }
        final CombinedDependencyNode combinedNode = new CombinedDependencyNode();
        final DependencyLink overrideLink = this.addLink(DependencyLink.Type.OVERRIDE, node, combinedNode);
        this.m_overrideLinks.put(overrideDeclaration, overrideLink);
        for (final Module module : overrideList) {
            this.addLink(DependencyLink.Type.REGULAR, combinedNode, new ModuleDependencyNode(module));
        }
        this.m_resolvedNodes.add(combinedNode);
    }
}
