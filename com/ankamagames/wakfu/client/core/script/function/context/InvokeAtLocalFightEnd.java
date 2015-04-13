package com.ankamagames.wakfu.client.core.script.function.context;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.keplerproject.luajava.*;

public class InvokeAtLocalFightEnd extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public InvokeAtLocalFightEnd(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "invokeAtLocalFightEnd";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("func", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final LuaScript script = this.getScriptObject();
        final String func = this.getParamString(0);
        final LuaValue[] params = this.getParams(1, paramCount);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isOnFight()) {
            script.tryToMakeIdle();
            final Fight fight = localPlayer.getCurrentFight();
            fight.addFightEndListener(new FightEndListener() {
                @Override
                public void onFightEnd(final Fight fight) {
                    script.runFunction(func, params, new LuaTable[0]);
                }
            });
        }
        else {
            script.runFunction(func, params, new LuaTable[0]);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)InvokeAtLocalFightEnd.class);
    }
}
