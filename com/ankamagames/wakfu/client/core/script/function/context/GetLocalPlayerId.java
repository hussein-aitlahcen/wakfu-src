package com.ankamagames.wakfu.client.core.script.function.context;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.*;
import org.keplerproject.luajava.*;

public final class GetLocalPlayerId extends JavaFunctionEx
{
    public GetLocalPlayerId(final LuaState state) {
        super(state);
    }
    
    @Override
    public String getName() {
        return "getPlayer";
    }
    
    @Override
    public String getDescription() {
        return "Fournit l'id du localPlayer";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("localPlayerId", "Id du joueur local", LuaScriptParameterType.LONG, false) };
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        this.addReturnValue(WakfuGameEntity.getInstance().getLocalPlayer().getId());
    }
}
