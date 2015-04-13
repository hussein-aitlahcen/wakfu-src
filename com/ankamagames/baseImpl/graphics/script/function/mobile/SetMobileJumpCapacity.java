package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileJumpCapacity extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileJumpCapacity";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileJumpCapacity(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileJumpCapacity";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileJumpCapacity.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final short jumpCapacity = (short)this.getParamInt(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null && mobile instanceof PathMobile) {
            ((PathMobile)mobile).setJumpCapacity(jumpCapacity);
        }
        else {
            this.writeError(SetMobileJumpCapacity.m_logger, "le mobile " + mobileId + " n'existe pas ou n'est pas un PathMobile");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileJumpCapacity.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("jumpCapacity", null, LuaScriptParameterType.INTEGER, false) };
    }
}
