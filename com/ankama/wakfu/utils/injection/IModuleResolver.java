package com.ankama.wakfu.utils.injection;

import com.google.inject.*;

public interface IModuleResolver
{
    Module resolveDependencies(Module... p0);
    
     <T extends Module> IModuleResolver usingImplementation(Class<T> p0, T p1);
    
    IModuleResolver addOverride(Module... p0);
    
    IModuleResolver addOverride(Class<? extends Module> p0, Module... p1);
    
    IModuleResolver setOverride(Class<? extends Module> p0, Module... p1);
}
