package com.ankamagames.wakfu.client.core.script.function.context;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.*;
import org.keplerproject.luajava.*;

public final class GetLocalPlayerName extends JavaFunctionEx
{
    public GetLocalPlayerName(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getPlayerName";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("localPlayerName", null, LuaScriptParameterType.STRING, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        this.addReturnValue(WakfuGameEntity.getInstance().getLocalPlayer().getName());
    }
}
