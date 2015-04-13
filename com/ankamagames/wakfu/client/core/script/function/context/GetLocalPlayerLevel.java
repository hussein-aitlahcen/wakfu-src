package com.ankamagames.wakfu.client.core.script.function.context;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.*;
import org.keplerproject.luajava.*;

public final class GetLocalPlayerLevel extends JavaFunctionEx
{
    public GetLocalPlayerLevel(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getPlayerLevel";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("localPlayerLevel", null, LuaScriptParameterType.LONG, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        this.addReturnValue(WakfuGameEntity.getInstance().getLocalPlayer().getLevel());
    }
}
