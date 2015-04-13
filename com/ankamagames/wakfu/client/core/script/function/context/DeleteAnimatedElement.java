package com.ankamagames.wakfu.client.core.script.function.context;

import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.keplerproject.luajava.*;

public class DeleteAnimatedElement extends JavaFunctionEx
{
    public DeleteAnimatedElement(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "deleteAnimatedElement";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", null, LuaScriptParameterType.LONG, false) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long id = this.getParamLong(0);
        SimpleAnimatedElementManager.getInstance().removeAnimatedElement(id);
    }
}
