package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileMovementStyle extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileMovementStyle";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileMovementStyle(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileMovementStyle";
    }
    
    @Override
    public String getDescription() {
        return "D?finit le style de mouvement d'un mobile (WALK, RUN, SLIDE, SWIM, WALK_CARRY).";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileMovementStyle.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final String walkStyleName = this.getParamString(1);
        final String runStyleName = (paramCount > 2) ? this.getParamString(2) : walkStyleName;
        final boolean justOnce = paramCount <= 3 || this.getParamBool(3);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null && mobile instanceof PathMobile) {
            final PathMobile pathMobile = (PathMobile)mobile;
            pathMobile.setMovementSelector(justOnce, walkStyleName, runStyleName);
        }
        else {
            this.writeError(SetMobileMovementStyle.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileMovementStyle.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("walkStyle", "Style de d?placement ? utiliser pour la marche", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("runStyle", "Style de d?placement ? utiliser pour la course", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("uniqueUsage", null, LuaScriptParameterType.BOOLEAN, true) };
    }
}
