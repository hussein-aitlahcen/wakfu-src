package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetPartVisible extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setPartVisible";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetPartVisible(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setPartVisible";
    }
    
    @Override
    public String getDescription() {
        return "Change la visibilit? d'une 'part' du mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetPartVisible.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            this.writeError(SetPartVisible.m_logger, "le mobile " + mobileId + " n'existe pas ");
            return;
        }
        final boolean visible = this.getParamBool(1);
        final String[] parts = new String[paramCount - 2];
        for (int i = 2; i < paramCount; ++i) {
            parts[i - 2] = this.getParamString(i);
        }
        mobile.setLinkageVisible(parts, visible);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetPartVisible.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("visible", "Visibilit?", LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("partNames", "Noms des 'parts'", LuaScriptParameterType.BLOOPS, false) };
    }
}
