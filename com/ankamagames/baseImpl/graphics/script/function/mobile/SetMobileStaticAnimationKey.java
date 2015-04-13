package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileStaticAnimationKey extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final boolean DEBUG = true;
    private static final String NAME = "setMobileAnimationStaticKey";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileStaticAnimationKey(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileAnimationStaticKey";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileStaticAnimationKey.PARAMS;
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
            final String animation = (paramCount == 2) ? this.getParamString(1) : "AnimStatique";
            mobile.setStaticAnimationKey(animation);
        }
        else {
            this.writeError(SetMobileStaticAnimationKey.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileStaticAnimationKey.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("animName", null, LuaScriptParameterType.STRING, true) };
    }
}
