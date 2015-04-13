package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class AddMobileCreationCallback extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "addMobileCreationCallback";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public AddMobileCreationCallback(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "addMobileCreationCallback";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return AddMobileCreationCallback.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final LuaScript script = this.getScriptObject();
        final long mobileId = this.getParamLong(0);
        final String func = this.getParamString(1);
        final LuaValue[] params = this.getParams(2, paramCount);
        if (MobileManager.getInstance().getMobile(mobileId) != null) {
            script.runFunction(func, params, new LuaTable[0]);
        }
        MobileManager.getInstance().addCreationListener(new MobileCreationListener() {
            @Override
            public void onMobileCreation(final Mobile mobile) {
                if (mobile.getId() == mobileId) {
                    script.runFunction(func, params, new LuaTable[0]);
                }
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddMobileCreationCallback.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
    }
}
