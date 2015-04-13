package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class GetMobileHeight extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "getMobileHeight";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public GetMobileHeight(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getMobileHeight";
    }
    
    @Override
    public String getDescription() {
        return "R?cup?re la hauteur d'un mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetMobileHeight.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetMobileHeight.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            this.addReturnValue(mobile.getHeight());
        }
        else {
            this.writeError(GetMobileHeight.m_logger, "le mobile " + mobileId + " n'existe pas ");
            this.addReturnNilValue();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetMobileHeight.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "L'Id du mobile", LuaScriptParameterType.LONG, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("height", "La hauteur du mobile", LuaScriptParameterType.INTEGER, false) };
    }
}
