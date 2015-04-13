package com.ankama.wakfu.utils.injection;

import com.google.common.base.*;

class DependencyLink
{
    public static final Function<DependencyLink, DependencyNode> LINK_GET_TARGET;
    private final Type m_type;
    private final DependencyNode m_source;
    private final DependencyNode m_target;
    
    public static Predicate<DependencyLink> ofType(final Type type) {
        return (Predicate<DependencyLink>)new Predicate<DependencyLink>() {
            public boolean apply(final DependencyLink input) {
                return input.getType() == type;
            }
        };
    }
    
    DependencyLink(final Type type, final DependencyNode source, final DependencyNode target) {
        super();
        this.m_type = (Type)Preconditions.checkNotNull((Object)type);
        this.m_source = (DependencyNode)Preconditions.checkNotNull((Object)source);
        this.m_target = (DependencyNode)Preconditions.checkNotNull((Object)target);
    }
    
    public Type getType() {
        return this.m_type;
    }
    
    public DependencyNode getSource() {
        return this.m_source;
    }
    
    public DependencyNode getTarget() {
        return this.m_target;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.m_source.hashCode();
        result = 31 * result + this.m_target.hashCode();
        result = 31 * result + this.m_type.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final DependencyLink other = (DependencyLink)obj;
        return this.m_source.equals(other.m_source) && this.m_target.equals(other.m_target) && this.m_type == other.m_type;
    }
    
    @Override
    public String toString() {
        return this.m_type + "[" + this.m_source + " -> " + this.m_target + "]";
    }
    
    static {
        LINK_GET_TARGET = (Function)new Function<DependencyLink, DependencyNode>() {
            public DependencyNode apply(final DependencyLink input) {
                return input.getTarget();
            }
        };
    }
    
    public enum Type
    {
        REGULAR, 
        OVERRIDE;
    }
}
