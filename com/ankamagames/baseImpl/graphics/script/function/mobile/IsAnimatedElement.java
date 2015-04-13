package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class IsAnimatedElement extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "isAnimatedElement";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public IsAnimatedElement(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "isAnimatedElement";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return IsAnimatedElement.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return IsAnimatedElement.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        if (MobileManager.getInstance().getMobile(mobileId) != null) {
            this.addReturnValue(true);
            return;
        }
        this.addReturnValue(SimpleAnimatedElementManager.getInstance().getAnimatedElement(mobileId) != null);
    }
    
    static {
        m_logger = Logger.getLogger((Class)IsAnimatedElement.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("isAnimatedElement", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
