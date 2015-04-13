package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileNext4Direction extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileNext4Direction";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileNext4Direction(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileNext4Direction";
    }
    
    @Override
    public String getDescription() {
        return "Oriente un mobile pour qu il soit dans une direction 4";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileNext4Direction.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null && !mobile.getDirection().isDirection4()) {
            mobile.setDirection(Direction8.getDirectionFromIndex((mobile.getDirection().m_index + 1) % 8));
        }
        else if (mobile == null) {
            this.writeError(SetMobileNext4Direction.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileNext4Direction.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false) };
    }
}
