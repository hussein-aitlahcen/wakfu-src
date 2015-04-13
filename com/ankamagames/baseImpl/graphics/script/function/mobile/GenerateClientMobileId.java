package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class GenerateClientMobileId extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "generateClientMobileId";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public GenerateClientMobileId(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "generateClientMobileId";
    }
    
    @Override
    public String getDescription() {
        return "G?n?re un id unique pour cr?er un mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GenerateClientMobileId.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GenerateClientMobileId.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        this.addReturnValue(GUIDGenerator.getGUID());
    }
    
    static {
        m_logger = Logger.getLogger((Class)GenerateClientMobileId.class);
        PARAMS = new LuaScriptParameterDescriptor[0];
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id unique", LuaScriptParameterType.LONG, false) };
    }
}
