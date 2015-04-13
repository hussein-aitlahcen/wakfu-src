package com.ankama.wakfu.utils.injection;

import com.google.inject.*;
import com.google.common.base.*;

class ModuleDependencyNode extends DependencyNode
{
    public static final Function<ModuleDependencyNode, Module> NODE_GET_MODULE;
    private final Module m_module;
    
    public Module getModule() {
        return this.m_module;
    }
    
    ModuleDependencyNode(final Module module) {
        super();
        this.m_module = (Module)Preconditions.checkNotNull((Object)module);
    }
    
    @Override
    public int hashCode() {
        return ModuleEquality.getHashCode(this.m_module);
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
        final ModuleDependencyNode other = (ModuleDependencyNode)obj;
        return ModuleEquality.areEqual(this.m_module, other.m_module);
    }
    
    @Override
    public String toString() {
        return "M[" + this.m_module.getClass().getName() + "]";
    }
    
    static {
        NODE_GET_MODULE = (Function)new Function<ModuleDependencyNode, Module>() {
            public Module apply(final ModuleDependencyNode input) {
                return input.m_module;
            }
        };
    }
}
