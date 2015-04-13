package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobilePosition extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobilePosition";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobilePosition(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobilePosition";
    }
    
    @Override
    public String getDescription() {
        return "Change la position du mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobilePosition.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final float worldX = this.getParamFloat(1);
        final float worldY = this.getParamFloat(2);
        final float altitude = this.getParamFloat(3);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            mobile.setWorldPosition(worldX, worldY, altitude);
        }
        else {
            this.writeError(SetMobilePosition.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobilePosition.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("worldX", "Positiuon x", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("worldY", "Position y", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("altitude", "Position z", LuaScriptParameterType.NUMBER, false) };
    }
}
