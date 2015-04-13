package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class GetMobileRadius extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "getMobileRadius";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public GetMobileRadius(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getMobileRadius";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetMobileRadius.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetMobileRadius.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            this.addReturnValue(mobile.getRenderRadius());
        }
        else {
            this.writeError(GetMobileRadius.m_logger, "le mobile " + mobileId + " n'existe pas ");
            this.addReturnNilValue();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetMobileRadius.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("radius", null, LuaScriptParameterType.NUMBER, false) };
    }
}
