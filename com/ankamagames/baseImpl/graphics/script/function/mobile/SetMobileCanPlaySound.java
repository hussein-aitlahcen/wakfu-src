package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileCanPlaySound extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileCanPlaySound";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileCanPlaySound(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileCanPlaySound";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileCanPlaySound.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final boolean canPlaySound = this.getParamBool(1);
        AnimatedElement mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            mobile = SimpleAnimatedElementManager.getInstance().getAnimatedElement(mobileId);
        }
        if (mobile != null) {
            mobile.setCanPlaySound(canPlaySound);
        }
        else {
            this.writeError(SetMobileCanPlaySound.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileCanPlaySound.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("canPlaySound", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
