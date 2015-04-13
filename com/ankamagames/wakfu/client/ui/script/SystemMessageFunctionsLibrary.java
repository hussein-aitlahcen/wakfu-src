package com.ankamagames.wakfu.client.ui.script;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.systemMessage.*;
import org.keplerproject.luajava.*;

public class SystemMessageFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final SystemMessageFunctionsLibrary m_instance;
    
    public static SystemMessageFunctionsLibrary getInstance() {
        return SystemMessageFunctionsLibrary.m_instance;
    }
    
    @Override
    public final String getName() {
        return "SystemMessage";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new AddSystemMessage(luaState) };
    }
    
    static {
        m_instance = new SystemMessageFunctionsLibrary();
    }
    
    private static class AddSystemMessage extends JavaFunctionEx
    {
        private AddSystemMessage(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addMessage";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("message", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            String message;
            if (paramCount == 1) {
                message = WakfuTranslator.getInstance().getString(this.getParamString(0));
            }
            else {
                final String[] params = new String[paramCount - 1];
                for (int i = 1; i < paramCount; ++i) {
                    final String param = this.getParamForcedAsString(i);
                    params[i - 1] = param;
                }
                message = WakfuTranslator.getInstance().getString(this.getParamString(0), (Object[])params);
            }
            final int duration = 1000 + 50 * message.length();
            WakfuSystemMessageManager.getInstance().showMessage(new SystemMessageData(WakfuSystemMessageManager.SystemMessageType.WORLD_INFO, message, duration));
        }
    }
}
