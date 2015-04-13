package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileDirection extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileDirection(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileDirection";
    }
    
    @Override
    public String getDescription() {
        return "Change la direction du mobile dans un syst?me ? 8 directions.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileDirection.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final int directionIndex = this.getParamInt(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            mobile.setDirection(Direction8.getDirectionFromIndex(directionIndex));
        }
        else {
            this.writeError(SetMobileDirection.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileDirection.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("directionIndex", "Nouvelle direction du mobile", LuaScriptParameterType.INTEGER, false) };
    }
}
