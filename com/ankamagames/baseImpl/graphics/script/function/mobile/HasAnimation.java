package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class HasAnimation extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "hasAnimation";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public HasAnimation(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "hasAnimation";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return HasAnimation.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return HasAnimation.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            this.writeError(HasAnimation.m_logger, "le mobile " + mobileId + "n'existe pas");
            this.addReturnNilValue();
            return;
        }
        final String anim = this.getParamString(1);
        final boolean res = mobile.getAnmInstance().containsAnimation(mobile.getFullAnimName(anim));
        this.addReturnValue(res);
    }
    
    static {
        m_logger = Logger.getLogger((Class)HasAnimation.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("animationName", null, LuaScriptParameterType.STRING, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("res", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
