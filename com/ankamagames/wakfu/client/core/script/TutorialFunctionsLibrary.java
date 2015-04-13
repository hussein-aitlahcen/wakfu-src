package com.ankamagames.wakfu.client.core.script;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.game.embeddedTutorial.*;
import org.keplerproject.luajava.*;

public class TutorialFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final Logger m_logger;
    private static final TutorialFunctionsLibrary m_instance;
    
    public static TutorialFunctionsLibrary getInstance() {
        return TutorialFunctionsLibrary.m_instance;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new EnableTutorialTooltips(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public final String getName() {
        return "Tutorial";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        m_logger = Logger.getLogger((Class)TutorialFunctionsLibrary.class);
        m_instance = new TutorialFunctionsLibrary();
    }
    
    private static class EnableTutorialTooltips extends JavaFunctionEx
    {
        public EnableTutorialTooltips(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "enableTutorialTooltips";
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
            EmbeddedTutorialManager.getInstance().setEnabled(this.getParamBool(0));
        }
    }
}
