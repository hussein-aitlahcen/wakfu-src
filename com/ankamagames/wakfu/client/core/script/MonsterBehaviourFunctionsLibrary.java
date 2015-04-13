package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class MonsterBehaviourFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final LuaScriptParameterDescriptor[] GET_MONSTER_RESULTS;
    private static final LuaScriptParameterDescriptor[] GET_TARGET_RESULTS;
    private static final LuaScriptParameterDescriptor[] GET_RESOURCE_POSITION_RESULTS;
    private static final LuaScriptParameterDescriptor[] IS_MONSTER_ON_FIGHT_RESULTS;
    private final MonsterBehaviourAction m_behaviourAction;
    
    @Override
    public final String getName() {
        return "Behaviour";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    public MonsterBehaviourFunctionsLibrary(final MonsterBehaviourAction action) {
        super();
        this.m_behaviourAction = action;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new GetMonster(luaState), new GetTarget(luaState), new GetResourcePosition(luaState), new IsMonsterOnFight(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    static {
        GET_MONSTER_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("monsterId", null, LuaScriptParameterType.LONG, false) };
        GET_TARGET_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("monsterId", null, LuaScriptParameterType.LONG, false) };
        GET_RESOURCE_POSITION_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false) };
        IS_MONSTER_ON_FIGHT_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("isOnFight", null, LuaScriptParameterType.BOOLEAN, false) };
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
            return MonsterBehaviourFunctionsLibrary.GET_MONSTER_RESULTS;
        }
        
        public void run(final int paramCount) throws LuaException {
            final NonPlayerCharacter npc = MonsterBehaviourFunctionsLibrary.this.m_behaviourAction.getNpc();
            if (npc != null) {
                this.addReturnValue(npc.getId());
            }
            else {
                this.addReturnNilValue();
            }
        }
    }
    
    private class GetTarget extends JavaFunctionEx
    {
        GetTarget(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getTarget";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return MonsterBehaviourFunctionsLibrary.GET_TARGET_RESULTS;
        }
        
        public void run(final int paramCount) throws LuaException {
            final CharacterInfo target = MonsterBehaviourFunctionsLibrary.this.m_behaviourAction.getTarget();
            if (target != null) {
                this.addReturnValue(target.getId());
            }
            else {
                this.addReturnNilValue();
            }
        }
    }
    
    private class GetResourcePosition extends JavaFunctionEx
    {
        GetResourcePosition(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getResourcePosition";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return MonsterBehaviourFunctionsLibrary.GET_RESOURCE_POSITION_RESULTS;
        }
        
        public void run(final int paramCount) throws LuaException {
            final long position = MonsterBehaviourFunctionsLibrary.this.m_behaviourAction.getResourcePosition();
            this.addReturnValue(MathHelper.getFirstIntFromLong(position));
            this.addReturnValue(MathHelper.getSecondIntFromLong(position));
        }
    }
    
    private class IsMonsterOnFight extends JavaFunctionEx
    {
        IsMonsterOnFight(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "isMonsterOnFight";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return MonsterBehaviourFunctionsLibrary.IS_MONSTER_ON_FIGHT_RESULTS;
        }
        
        public void run(final int paramCount) throws LuaException {
            final NonPlayerCharacter npc = MonsterBehaviourFunctionsLibrary.this.m_behaviourAction.getNpc();
            if (npc != null) {
                this.addReturnValue(npc.isOnFight());
            }
            else {
                this.addReturnNilValue();
            }
        }
    }
}
