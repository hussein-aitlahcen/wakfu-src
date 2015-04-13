package com.ankamagames.wakfu.client.core.script.function.context;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.*;
import org.keplerproject.luajava.*;

public final class GetLocalPlayerInstanceId extends JavaFunctionEx
{
    public GetLocalPlayerInstanceId(final LuaState state) {
        super(state);
    }
    
    @Override
    public String getName() {
        return "getPlayerInstanceId";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("localPlayerInstanceId", null, LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        this.addReturnValue(WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId());
    }
}
