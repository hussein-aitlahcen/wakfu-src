package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileToStaticAnimation extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileToStaticAnimation";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileToStaticAnimation(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileToStaticAnimation";
    }
    
    @Override
    public String getDescription() {
        return "Remet un mobile dans une pose statique, c'est-?-dire dans une animation par d?faut.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileToStaticAnimation.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            mobile.setAnimation(mobile.getStaticAnimationKey());
        }
        else {
            this.writeError(SetMobileToStaticAnimation.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileToStaticAnimation.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false) };
    }
}
