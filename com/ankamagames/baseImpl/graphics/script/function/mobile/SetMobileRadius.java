package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileRadius extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileRadius";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public SetMobileRadius(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileRadius";
    }
    
    @Override
    public String getDescription() {
        return "Change le rayon du mobile pour permettre le multi-case";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileRadius.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return SetMobileRadius.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final float radius = this.getParamFloat(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            this.addReturnValue(mobile.getRenderRadius());
            mobile.setRenderRadius(radius);
            final Mobile carried = mobile.getCarriedMobile();
            if (carried != null) {
                carried.setRenderRadius(radius);
            }
        }
        else {
            this.writeError(SetMobileRadius.m_logger, "le mobile " + mobileId + " n'existe pas ");
            this.addReturnNilValue();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileRadius.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("radius", "Nouveau rayon", LuaScriptParameterType.NUMBER, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("oldRadius", "Ancien rayon", LuaScriptParameterType.NUMBER, false) };
    }
}
