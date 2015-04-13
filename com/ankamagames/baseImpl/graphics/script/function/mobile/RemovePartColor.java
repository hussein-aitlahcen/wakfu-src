package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class RemovePartColor extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "removePartColor";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public RemovePartColor(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "removePartColor";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return RemovePartColor.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final String partName = this.getParamString(1);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            final int index = Anm.getColorIndex(partName);
            if (index == 0) {
                this.writeError(RemovePartColor.m_logger, "partName invalid " + partName);
                return;
            }
            mobile.getAnmInstance().removeCustomColor(index);
        }
        else {
            this.writeError(RemovePartColor.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)RemovePartColor.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("partName", null, LuaScriptParameterType.STRING, false) };
    }
}
