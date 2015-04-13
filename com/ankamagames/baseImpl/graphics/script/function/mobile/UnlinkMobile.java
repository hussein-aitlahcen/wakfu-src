package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class UnlinkMobile extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "unlinkMobile";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public UnlinkMobile(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "unlinkMobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return UnlinkMobile.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            mobile.unlinkChildrenMobile();
        }
        else {
            this.writeError(UnlinkMobile.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileNext4Direction.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false) };
    }
}
