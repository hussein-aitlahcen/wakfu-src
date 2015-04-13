package com.ankamagames.baseImpl.graphics.script.function.world;

import com.ankamagames.framework.graphics.engine.fadeManager.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class RunOnFadeOut extends JavaFunctionEx
{
    public RunOnFadeOut(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "runOnFadeOut";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("func", null, LuaScriptParameterType.STRING, false) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        String endFunc = null;
        final LuaObject obj = this.getParam(2);
        if (obj.isString()) {
            endFunc = obj.getString();
        }
        if (FadeManager.getInstance().isDisabledOut()) {
            this.getScriptObject().runFunction(endFunc);
        }
        else {
            FadeManager.getInstance().addListener(new FadeOutListener(endFunc, this.getScriptObject(), null));
        }
    }
    
    public static class FadeOutListener implements FadeListener
    {
        private final String m_func;
        private final LuaScript m_script;
        private final LuaValue[] m_params;
        
        public FadeOutListener(final String func, final LuaScript script, final LuaValue[] params) {
            super();
            assert script != null : "FadeOutListener avec un script null";
            assert func != null : "FadeOutListener avec une fonction nulle";
            this.m_func = func;
            this.m_params = params;
            this.m_script = script;
        }
        
        @Override
        public void onFadeInEnd() {
        }
        
        @Override
        public void onFadeOutEnd() {
            this.m_script.runFunction(this.m_func, this.m_params, new LuaTable[0]);
            FadeManager.getInstance().removeListener(this);
        }
        
        @Override
        public void onFadeInStart() {
        }
        
        @Override
        public void onFadeOutStart() {
        }
    }
}
