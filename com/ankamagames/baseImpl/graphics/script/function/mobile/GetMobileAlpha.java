package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class GetMobileAlpha extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "getMobileAlpha";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public GetMobileAlpha(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getMobileAlpha";
    }
    
    @Override
    public String getDescription() {
        return "Retourne l'opacit? du mobile.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetMobileAlpha.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetMobileAlpha.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            this.addReturnValue(mobile.getAlpha());
        }
        else {
            this.writeError(GetMobileAlpha.m_logger, "le mobile " + mobileId + " n'existe pas ");
            this.addReturnNilValue();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetMobileAlpha.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("alpha", "opacit? du personnage", LuaScriptParameterType.NUMBER, false) };
    }
}
