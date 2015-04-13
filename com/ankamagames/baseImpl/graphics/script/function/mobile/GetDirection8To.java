package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public final class GetDirection8To extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "getDirection8To";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public GetDirection8To(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getDirection8To";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetDirection8To.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetDirection8To.RESULT;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final int startX = this.getParamInt(0);
        final int startY = this.getParamInt(1);
        final int destX = this.getParamInt(2);
        final int destY = this.getParamInt(3);
        final int diffX = destX - startX;
        final int diffY = destY - startY;
        final Direction8 dir = Direction8.getDirectionFromVector(MathHelper.clamp(diffX, -1, 1), MathHelper.clamp(diffY, -1, 1));
        if (dir == null) {
            GetDirection8To.m_logger.error((Object)("Pas de direction trouv?e pour les coords : " + startX + ", " + startY + ", " + destX + ", " + destY));
            this.addReturnValue(0);
        }
        else {
            this.addReturnValue(dir.m_index);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetDirection8To.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("startX", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("startY", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("destX", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("destY", null, LuaScriptParameterType.NUMBER, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("direction", null, LuaScriptParameterType.INTEGER, false) };
    }
}
