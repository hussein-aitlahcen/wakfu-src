package com.ankamagames.framework.kernel.gameStats;

import java.util.*;

public abstract class ContainerNode extends Node
{
    protected final Map<String, Node> m_children;
    
    protected ContainerNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
        this.m_children = new HashMap<String, Node>();
    }
    
    protected <NT extends Node> NT addChild(final NT child) {
        final String n = child.getName();
        final String name = (n != null) ? n.intern() : null;
        if (this.m_children.containsKey(name)) {
            throw new RuntimeException("Le noeud " + this + " poss\u00e8de d\u00e9j\u00e0 un enfant nomm\u00e9 " + child.getName());
        }
        this.m_children.put(name, child);
        return child;
    }
    
    @Override
    public final Map<String, Node> getDirectChildren() {
        return this.m_children;
    }
    
    @Override
    public void clear() {
        for (final Node node : this.m_children.values()) {
            node.clear();
        }
        this.m_children.clear();
    }
    
    @Override
    public final boolean hasValue() {
        return false;
    }
    
    @Override
    public long getValue() {
        throw new UnsupportedOperationException("Pas de valeur pour un ContainerNode");
    }
}
