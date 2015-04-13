package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetPartColor extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setPartColor";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetPartColor(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setPartColor";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetPartColor.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final String partName = this.getParamString(1);
        final float r = (float)this.getParamDouble(2);
        final float g = (float)this.getParamDouble(3);
        final float b = (float)this.getParamDouble(4);
        final float a = (float)this.getParamDouble(5);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            final int index = Anm.getColorIndex(partName);
            if (index == 0) {
                this.writeError(SetPartColor.m_logger, "partName invalid " + partName);
                return;
            }
            mobile.setCustomColor(index, new float[] { r, g, b, a });
        }
        else {
            this.writeError(SetPartColor.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetPartColor.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("partName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("red", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("green", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("blue", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("alpha", null, LuaScriptParameterType.NUMBER, false) };
    }
}
