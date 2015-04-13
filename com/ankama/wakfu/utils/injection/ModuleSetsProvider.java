package com.ankama.wakfu.utils.injection;

import com.google.inject.*;
import java.util.*;
import com.google.common.base.*;
import com.google.common.collect.*;

class ModuleSetsProvider
{
    private final ModuleDependencyGraph m_graph;
    private final Set<DependencyNode> m_visitedNodes;
    
    public List<Set<Module>> getModuleSets() {
        final ImmutableList.Builder<Set<Module>> builder = (ImmutableList.Builder<Set<Module>>)ImmutableList.builder();
        builder.add((Object)this.getModules(this.regularDependencies(this.m_graph.getRoot())));
        for (final DependencyLink dependencyLink : this.m_graph.getOrderedOverrideLinks()) {
            builder.add((Object)this.getModules(this.regularDependencies(dependencyLink.getTarget())));
        }
        return (List<Set<Module>>)builder.build();
    }
    
    private Set<Module> getModules(final Iterable<ModuleDependencyNode> dependencyNodes) {
        return (Set<Module>)FluentIterable.from((Iterable)dependencyNodes).transform((Function)ModuleDependencyNode.NODE_GET_MODULE).toSet();
    }
    
    ModuleSetsProvider(final ModuleDependencyGraph graph) {
        super();
        this.m_visitedNodes = (Set<DependencyNode>)Sets.newHashSet();
        this.m_graph = graph;
    }
    
    private Set<ModuleDependencyNode> regularDependencies(final DependencyNode startNode) {
        final Set<ModuleDependencyNode> dependencies = (Set<ModuleDependencyNode>)Sets.newHashSet();
        this.addRegularDependencies(dependencies, startNode);
        return dependencies;
    }
    
    private void addRegularDependencies(final Set<ModuleDependencyNode> dependencies, final DependencyNode node) {
        if (this.m_visitedNodes.contains(node)) {
            return;
        }
        if (node instanceof ModuleDependencyNode) {
            dependencies.add((ModuleDependencyNode)node);
        }
        this.m_visitedNodes.add(node);
        for (final DependencyLink link : node.getLinks()) {
            if (link.getType() != DependencyLink.Type.REGULAR) {
                continue;
            }
            this.addRegularDependencies(dependencies, link.getTarget());
        }
    }
}
