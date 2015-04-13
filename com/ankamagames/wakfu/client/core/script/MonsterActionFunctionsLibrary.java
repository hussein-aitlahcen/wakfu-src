package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class MonsterActionFunctionsLibrary extends JavaFunctionsLibrary
{
    private final MonsterActionAction m_monsterAction;
    
    @Override
    public final String getName() {
        return "MonsterAction";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    public MonsterActionFunctionsLibrary(final MonsterActionAction action) {
        super();
        this.m_monsterAction = action;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new GetMonster(luaState), new GetPlayer(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    private class GetMonster extends JavaFunctionEx
    {
        GetMonster(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getMonster";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("monsterId", null, LuaScriptParameterType.LONG, false) };
        }
        
        public void run(final int paramCount) throws LuaException {
            final NonPlayerCharacter npc = MonsterActionFunctionsLibrary.this.m_monsterAction.getNpc();
            if (npc != null) {
                this.addReturnValue(npc.getId());
            }
            else {
                this.addReturnNilValue();
            }
        }
    }
    
    private class GetPlayer extends JavaFunctionEx
    {
        GetPlayer(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getPlayer";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("playerId", null, LuaScriptParameterType.LONG, false) };
        }
        
        public void run(final int paramCount) throws LuaException {
            final CharacterInfo target = MonsterActionFunctionsLibrary.this.m_monsterAction.getPlayer();
            if (target != null) {
                this.addReturnValue(target.getId());
            }
            else {
                this.addReturnNilValue();
            }
        }
    }
}
