package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class GetMobileStatus extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "getMobileStatus";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public GetMobileStatus(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getMobileStatus";
    }
    
    @Override
    public String getDescription() {
        return "Retourne l'?tat du mobile.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetMobileStatus.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetMobileStatus.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            this.addReturnValue(mobile.getStatus());
        }
        else {
            this.writeError(GetMobileStatus.m_logger, "le mobile " + mobileId + " n'existe pas ");
            this.addReturnNilValue();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetMobileStatus.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "L'Id du mobile", LuaScriptParameterType.LONG, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("status", "Status du mobile (0 = NORMAL, 1 = KO, 2 = DEAD)", LuaScriptParameterType.INTEGER, false) };
    }
}
