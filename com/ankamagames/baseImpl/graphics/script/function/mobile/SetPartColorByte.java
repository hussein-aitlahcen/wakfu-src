package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetPartColorByte extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setPartColorByte";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetPartColorByte(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setPartColorByte";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetPartColorByte.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final String partName = this.getParamString(1);
        final float r = this.getParamInt(2) / 255.0f;
        final float g = this.getParamInt(3) / 255.0f;
        final float b = this.getParamInt(4) / 255.0f;
        final float a = this.getParamInt(5) / 255.0f;
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            final int index = Anm.getColorIndex(partName);
            if (index == 0) {
                this.writeError(SetPartColorByte.m_logger, "partName invalid " + partName);
                return;
            }
            mobile.setCustomColor(index, new float[] { r, g, b, a });
        }
        else {
            this.writeError(SetPartColorByte.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetPartColorByte.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("partName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("red", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("green", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("blue", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("alpha", null, LuaScriptParameterType.INTEGER, false) };
    }
}
