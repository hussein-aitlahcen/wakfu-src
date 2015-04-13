package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class SetMobileStatus extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileStatus";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileStatus(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileStatus";
    }
    
    @Override
    public String getDescription() {
        return "Change le status d'un mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileStatus.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final byte status = (byte)this.getParamInt(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            mobile.setStatus(status);
        }
        else {
            this.writeError(SetMobileStatus.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileStatus.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("statusId", "Id du status (0 = NORMAL,  1 = KO, 2 = DEAD)", LuaScriptParameterType.INTEGER, false) };
    }
}
