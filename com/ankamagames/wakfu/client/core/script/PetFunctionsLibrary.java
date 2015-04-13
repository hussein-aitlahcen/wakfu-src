package com.ankamagames.wakfu.client.core.script;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import org.keplerproject.luajava.*;

public class PetFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final PetFunctionsLibrary m_instance;
    
    public static PetFunctionsLibrary getInstance() {
        return PetFunctionsLibrary.m_instance;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new GetPetId(luaState), new SetPetVisible(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public final String getName() {
        return "Pet";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        m_instance = new PetFunctionsLibrary();
    }
    
    private static class GetPetId extends JavaFunctionEx
    {
        GetPetId(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getPetId";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("playerId", null, LuaScriptParameterType.LONG, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("petId", null, LuaScriptParameterType.LONG, false) };
        }
        
        public void run(final int paramCount) throws LuaException {
            final long playerId = this.getParamLong(0);
            final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(playerId);
            if (character != null && character instanceof PlayerCharacter) {
                final PetMobileView pet = ((PlayerCharacter)character).getPetMobile();
                if (pet != null) {
                    this.addReturnValue(pet.getMobile().getId());
                }
                else {
                    this.addReturnNilValue();
                    this.writeError(PetFunctionsLibrary.m_logger, "le character " + playerId + " n'a pas de pet");
                }
            }
            else {
                this.addReturnNilValue();
                this.writeError(PetFunctionsLibrary.m_logger, "le character " + playerId + " n'existe pas ou n'est pas un joueur");
            }
        }
    }
    
    private static class SetPetVisible extends JavaFunctionEx
    {
        SetPetVisible(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setPetVisible";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("playerId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("visible", null, LuaScriptParameterType.BOOLEAN, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final long playerId = this.getParamLong(0);
            final boolean petVisible = this.getParamBool(1);
            final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(playerId);
            if (character != null && character instanceof PlayerCharacter) {
                final PetMobileView pet = ((PlayerCharacter)character).getPetMobile();
                if (pet != null) {
                    pet.getMobile().setVisible(petVisible);
                }
                else {
                    this.writeError(PetFunctionsLibrary.m_logger, "le character " + playerId + " n'a pas de pet");
                }
            }
            else {
                this.writeError(PetFunctionsLibrary.m_logger, "le character " + playerId + " n'existe pas ou n'est pas un joueur");
            }
        }
    }
}
