package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileAlpha extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileAlpha";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileAlpha(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileAlpha";
    }
    
    @Override
    public String getDescription() {
        return "Change l'opacit? d'un mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileAlpha.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final float alpha = (float)this.getParamDouble(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            mobile.setAlpha(alpha);
        }
        else {
            this.writeError(SetMobileAlpha.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileAlpha.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("alpha", "Valeur de l'opacit?", LuaScriptParameterType.NUMBER, false) };
    }
}
