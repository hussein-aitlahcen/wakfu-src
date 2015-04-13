package com.ankama.wakfu.utils.injection;

import com.google.inject.*;
import com.google.common.base.*;

class ModuleInterfaceDependencyNode extends DependencyNode
{
    private final Class<? extends Module> m_moduleInterface;
    
    ModuleInterfaceDependencyNode(final Class<? extends Module> moduleInterface) {
        super();
        this.m_moduleInterface = (Class<? extends Module>)Preconditions.checkNotNull((Object)moduleInterface);
    }
    
    public Class<? extends Module> getModuleInterface() {
        return this.m_moduleInterface;
    }
    
    @Override
    public int hashCode() {
        final int offset = 442292081;
        return 442292081 + this.m_moduleInterface.hashCode();
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
        final ModuleInterfaceDependencyNode other = (ModuleInterfaceDependencyNode)obj;
        return this.m_moduleInterface.equals(other.m_moduleInterface);
    }
    
    @Override
    public String toString() {
        return "I[" + this.m_moduleInterface.getName() + "]";
    }
}
