package com.ankamagames.wakfu.client.core.script;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import org.keplerproject.luajava.*;

public class ChallengeFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final Logger m_logger;
    private static final ChallengeFunctionsLibrary m_instance;
    
    public static ChallengeFunctionsLibrary getInstance() {
        return ChallengeFunctionsLibrary.m_instance;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new EnableAreaChallenges(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public final String getName() {
        return "Challenge";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChallengeFunctionsLibrary.class);
        m_instance = new ChallengeFunctionsLibrary();
    }
    
    private static class EnableAreaChallenges extends JavaFunctionEx
    {
        public EnableAreaChallenges(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "enableAreaChallenges";
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
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.AREA_CHALLENGES_ACTIVATED_KEY, this.getParamBool(0));
        }
    }
}
