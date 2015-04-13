package com.ankama.wakfu.utils.injection;

import com.google.inject.*;
import com.google.common.collect.*;

class OverrideDeclaration
{
    private final Class<? extends Module> m_overridenModuleType;
    private final ImmutableList<Module> m_overridingModules;
    
    OverrideDeclaration(final Class<? extends Module> overridenModuleType, final Iterable<? extends Module> overridingModules) {
        super();
        this.m_overridenModuleType = overridenModuleType;
        this.m_overridingModules = (ImmutableList<Module>)ImmutableList.copyOf((Iterable)overridingModules);
    }
    
    public Class<? extends Module> getOverridenModuleType() {
        return this.m_overridenModuleType;
    }
    
    public ImmutableList<Module> getOverridingModules() {
        return this.m_overridingModules;
    }
}
