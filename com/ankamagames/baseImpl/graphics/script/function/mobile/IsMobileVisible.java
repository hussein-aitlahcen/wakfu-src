package com.ankamagames.baseImpl.graphics.script.function.mobile;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class IsMobileVisible extends JavaFunctionEx
{
    private static final String NAME = "isMobileVisible";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public IsMobileVisible(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "isMobileVisible";
    }
    
    @Override
    public String getDescription() {
        return "indique si le mobile est visible";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return IsMobileVisible.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return IsMobileVisible.RESULT;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        this.addReturnValue(mobile != null && mobile.isVisible());
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "L'Id du mobile", LuaScriptParameterType.LONG, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("visible", "La visibilit? du mobile", LuaScriptParameterType.BOOLEAN, false) };
    }
}
