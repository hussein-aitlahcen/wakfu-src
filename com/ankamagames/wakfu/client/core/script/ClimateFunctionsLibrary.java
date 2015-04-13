package com.ankamagames.wakfu.client.core.script;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import org.keplerproject.luajava.*;

public class ClimateFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final Logger m_logger;
    private static final ClimateFunctionsLibrary m_instance;
    
    public static ClimateFunctionsLibrary getInstance() {
        return ClimateFunctionsLibrary.m_instance;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new Rain(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public final String getName() {
        return "Climate";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClimateFunctionsLibrary.class);
        m_instance = new ClimateFunctionsLibrary();
    }
    
    private static class Rain extends JavaFunctionEx
    {
        Rain(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "rain";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("enable", null, LuaScriptParameterType.BOOLEAN, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
        }
    }
}
