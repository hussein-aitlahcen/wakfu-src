package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileAnimationSpeed extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileAnimationSpeed";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileAnimationSpeed(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileAnimationSpeed";
    }
    
    @Override
    public String getDescription() {
        return "Change la vitesse d'ex?cution de l'animation d'un mobile.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileAnimationSpeed.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final float animationSpeed = (float)this.getParamDouble(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            mobile.setAnimationSpeed(animationSpeed);
        }
        else {
            this.writeError(SetMobileAnimationSpeed.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileAnimationSpeed.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("animationSpeed", "Nouvelle vitesse d'animation (1 = normale)", LuaScriptParameterType.NUMBER, false) };
    }
}
