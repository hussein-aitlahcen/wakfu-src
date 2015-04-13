package com.ankamagames.baseImpl.graphics.script;

import org.apache.log4j.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.function.world.*;

public class WorldFunctionsLibraries extends JavaFunctionsLibrary
{
    private static final Logger m_logger;
    private static final WorldFunctionsLibraries m_instance;
    
    public static WorldFunctionsLibraries getInstance() {
        return WorldFunctionsLibraries.m_instance;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new RunOnFadeIn(luaState), new RunOnFadeOut(luaState), new SetPictoAlpha(luaState), new SetPictoLayerAlpha(luaState), new SetPictoColor(luaState), new SetLoading(luaState), new SetVideoLoading(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public final String getName() {
        return "World";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorldFunctionsLibraries.class);
        m_instance = new WorldFunctionsLibraries();
    }
}
