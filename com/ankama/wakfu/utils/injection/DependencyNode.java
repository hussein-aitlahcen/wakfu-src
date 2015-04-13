package com.ankama.wakfu.utils.injection;

import com.google.common.base.*;
import java.util.*;
import com.google.common.collect.*;

abstract class DependencyNode
{
    public static final Function<DependencyNode, Iterable<DependencyLink>> NODE_GET_LINKS;
    public static final Function<DependencyNode, Iterable<DependencyNode>> NODE_GET_NEIGHBOURS;
    private final Set<DependencyLink> m_links;
    
    DependencyNode() {
        super();
        this.m_links = (Set<DependencyLink>)Sets.newLinkedHashSet();
    }
    
    public boolean addLink(final DependencyLink link) {
        return this.m_links.add(link);
    }
    
    public Set<DependencyLink> getLinks() {
        return (Set<DependencyLink>)ImmutableSet.copyOf((Collection)this.m_links);
    }
    
    static {
        NODE_GET_LINKS = (Function)new Function<DependencyNode, Iterable<DependencyLink>>() {
            public Iterable<DependencyLink> apply(final DependencyNode input) {
                return input.getLinks();
            }
        };
        NODE_GET_NEIGHBOURS = (Function)new Function<DependencyNode, Iterable<DependencyNode>>() {
            public Iterable<DependencyNode> apply(final DependencyNode input) {
                return (Iterable<DependencyNode>)Iterables.transform((Iterable)input.getLinks(), (Function)DependencyLink.LINK_GET_TARGET);
            }
        };
    }
}
