package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class MonsterEvolutionFunctionsLibrary extends JavaFunctionsLibrary
{
    private final MonsterEvolutionAction m_evolutionAction;
    
    @Override
    public final String getName() {
        return "Evolution";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    public MonsterEvolutionFunctionsLibrary(final MonsterEvolutionAction action) {
        super();
        this.m_evolutionAction = action;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new GetMonster(luaState), new GetEvolvedBreedId(luaState), new EvolveMonsterToBreed(luaState) };
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
            this.addReturnValue(MonsterEvolutionFunctionsLibrary.this.m_evolutionAction.getNpcId());
        }
    }
    
    private class GetEvolvedBreedId extends JavaFunctionEx
    {
        GetEvolvedBreedId(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getEvolvedBreedId";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("breedId", null, LuaScriptParameterType.INTEGER, false) };
        }
        
        public void run(final int paramCount) throws LuaException {
            this.addReturnValue(MonsterEvolutionFunctionsLibrary.this.m_evolutionAction.getEvolvedBreedId());
        }
    }
    
    private class EvolveMonsterToBreed extends JavaFunctionEx
    {
        EvolveMonsterToBreed(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "evolveMonsterToBreed";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("breedId", null, LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final short breedId = (short)this.getParamInt(0);
            final NonPlayerCharacter character = (NonPlayerCharacter)MonsterEvolutionFunctionsLibrary.this.m_evolutionAction.getNpc();
            if (character != null) {
                character.evolve(breedId, MonsterEvolutionFunctionsLibrary.this.m_evolutionAction.getEvolvedLevel());
            }
        }
    }
}
