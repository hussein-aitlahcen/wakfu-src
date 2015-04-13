package com.ankamagames.baseImpl.graphics.script;

import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.function.particle.*;

public class ParticleSystemFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final ParticleSystemFunctionsLibrary m_instance;
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new AddTweenParticleSystem(luaState), new AddParticleSystem(luaState), new RemoveParticleSystem(luaState), new AddParticleSystemToMobile(luaState), new TimeTweenParticleSystem(luaState), new AddParticleSystemToInteractiveElement(luaState), new AddParticleSystemToInteractiveElementWithOffset(luaState), new AddParticleSystemToMobileWithOffset(luaState), new ClearParticleSystemOnMobile(luaState), new SetRemoveWhenFar(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    public static ParticleSystemFunctionsLibrary getInstance() {
        return ParticleSystemFunctionsLibrary.m_instance;
    }
    
    @Override
    public final String getName() {
        return "Particle";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        m_instance = new ParticleSystemFunctionsLibrary();
    }
}
