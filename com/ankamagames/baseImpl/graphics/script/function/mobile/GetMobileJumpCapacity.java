package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class GetMobileJumpCapacity extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "getMobileJumpCapacity";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public GetMobileJumpCapacity(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getMobileJumpCapacity";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetMobileJumpCapacity.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetMobileJumpCapacity.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null && mobile instanceof PathMobile) {
            this.addReturnValue(((PathMobile)mobile).getJumpCapacity());
        }
        else {
            this.writeError(GetMobileJumpCapacity.m_logger, "le mobile " + mobileId + " n'existe pas ou n'est pas un PathMobile");
            this.addReturnNilValue();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetMobileJumpCapacity.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("jumpCapacity", null, LuaScriptParameterType.INTEGER, false) };
    }
}
