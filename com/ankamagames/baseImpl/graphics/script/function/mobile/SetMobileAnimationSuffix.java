package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileAnimationSuffix extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileAnimationSuffix";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileAnimationSuffix(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileAnimationSuffix";
    }
    
    @Override
    public String getDescription() {
        return "Sp?cifie un suffixe qui sera ajout? ? toutes les anims";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileAnimationSuffix.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            this.writeError(SetMobileAnimationSuffix.m_logger, "le mobile " + mobileId + " n'existe pas ");
            return;
        }
        mobile.setAnimationSuffix((paramCount == 2) ? this.getParamString(1) : null);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileAnimationSuffix.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("animationSuffix", "Suffixe des animations", LuaScriptParameterType.STRING, true) };
    }
}
