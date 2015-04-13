package com.ankamagames.baseImpl.graphics.script.function.world;

import com.ankamagames.framework.graphics.engine.fadeManager.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class RunOnFadeIn extends JavaFunctionEx
{
    public RunOnFadeIn(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "runOnFadeIn";
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
        final String endFunc = this.getParamString(0);
        if (FadeManager.getInstance().isDisabledIn()) {
            this.getScriptObject().runFunction(endFunc);
        }
        else {
            FadeManager.getInstance().addListener(new FadeInListener(endFunc, this.getScriptObject(), null));
        }
    }
    
    public static class FadeInListener implements FadeListener
    {
        private final String m_func;
        private final LuaScript m_script;
        private final LuaValue[] m_params;
        
        public FadeInListener(final String func, final LuaScript script, final LuaValue[] params) {
            super();
            assert script != null : "FadeInListener avec un script null";
            assert func != null : "FadeInListener avec une fonction nulle";
            this.m_func = func;
            this.m_params = params;
            this.m_script = script;
        }
        
        @Override
        public void onFadeInEnd() {
            this.m_script.runFunction(this.m_func, this.m_params, new LuaTable[0]);
            FadeManager.getInstance().removeListener(this);
        }
        
        @Override
        public void onFadeOutEnd() {
        }
        
        @Override
        public void onFadeInStart() {
        }
        
        @Override
        public void onFadeOutStart() {
        }
    }
}
