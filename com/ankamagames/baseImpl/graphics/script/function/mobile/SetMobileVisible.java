package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileVisible extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileVisible";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileVisible(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileVisible";
    }
    
    @Override
    public String getDescription() {
        return "Change la visibilit? d'un mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileVisible.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final boolean visible = this.getParamBool(1);
        AnimatedElement mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            mobile = SimpleAnimatedElementManager.getInstance().getAnimatedElement(mobileId);
        }
        if (mobile != null) {
            mobile.setVisible(visible);
        }
        else {
            this.writeError(SetMobileVisible.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileVisible.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("visible", "Visilit?", LuaScriptParameterType.BOOLEAN, false) };
    }
}
