package com.ankama.wakfu.utils.injection;

import com.google.common.collect.*;
import java.util.*;

class ModuleDependencyGraph
{
    private final ImmutableSet<DependencyNode> m_nodes;
    private final DependencyNode m_root;
    private final ImmutableList<DependencyLink> m_orderedOverrideLinks;
    
    ModuleDependencyGraph(final Set<DependencyNode> nodes, final DependencyNode root, final Iterable<DependencyLink> orderedOverrideLinks) {
        super();
        this.m_root = root;
        this.m_nodes = (ImmutableSet<DependencyNode>)ImmutableSet.copyOf((Collection)nodes);
        this.m_orderedOverrideLinks = (ImmutableList<DependencyLink>)ImmutableList.copyOf((Iterable)orderedOverrideLinks);
    }
    
    public DependencyNode getRoot() {
        return this.m_root;
    }
    
    public ImmutableSet<DependencyNode> getNodes() {
        return this.m_nodes;
    }
    
    public ImmutableList<DependencyLink> getOrderedOverrideLinks() {
        return this.m_orderedOverrideLinks;
    }
}
