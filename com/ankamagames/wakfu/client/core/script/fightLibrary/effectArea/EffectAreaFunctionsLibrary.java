package com.ankamagames.wakfu.client.core.script.fightLibrary.effectArea;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class EffectAreaFunctionsLibrary extends JavaFunctionsLibrary
{
    private final EffectAreaTriggeredAction m_effectAreaAction;
    
    public EffectAreaFunctionsLibrary(final EffectAreaTriggeredAction action) {
        super();
        this.m_effectAreaAction = action;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new GetTarget(luaState, this.m_effectAreaAction), new GetAreaPosition(luaState, this.m_effectAreaAction), new GetAreaId(luaState, this.m_effectAreaAction) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public final String getName() {
        return "EffectArea";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
}
