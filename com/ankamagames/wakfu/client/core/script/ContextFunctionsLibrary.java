package com.ankamagames.wakfu.client.core.script;

import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.script.function.context.*;

public class ContextFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final ContextFunctionsLibrary m_instance;
    
    public static ContextFunctionsLibrary getInstance() {
        return ContextFunctionsLibrary.m_instance;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new GetLocalPlayerId(luaState), new IsLocalPlayerInFight(luaState), new GetLocalPlayerName(luaState), new GetLocalPlayerLevel(luaState), new GetZ(luaState), new IsWalkable(luaState), new IsCellInFight(luaState), new GetFighterId(luaState), new SetCharacteristicListenerOn(luaState), new GetCharacteristicValue(luaState), new GetLocalPlayerPetId(luaState), new CreateAnimatedElement(luaState), new DeleteAnimatedElement(luaState), new InvokeAtLocalFightEnd(luaState), new GetCharacterName(luaState), new GetDistanceFromLocalPlayer(luaState), new IsCellBlockedByObstacle(luaState), new GetLocalPlayerInstanceId(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public final String getName() {
        return "Context";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        m_instance = new ContextFunctionsLibrary();
    }
}
