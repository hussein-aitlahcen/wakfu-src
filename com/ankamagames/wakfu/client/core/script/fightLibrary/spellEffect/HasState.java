package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

class HasState extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "hasState";
    private static final String DESC = "Renvoie true si le personnage dont l'id pass? en param porte l'?tat d'id et de niveau (optionnel) donn?";
    private static final LuaScriptParameterDescriptor[] HAS_STATE_PARAMS;
    private static final LuaScriptParameterDescriptor[] HAS_STATE_RESULTS;
    
    HasState(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "hasState";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return HasState.HAS_STATE_PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return HasState.HAS_STATE_RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long characterId = this.getParamLong(0);
        final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(characterId);
        if (info == null) {
            this.writeError(HasState.m_logger, "CharacterInfo introuvable : " + characterId);
            this.addReturnNilValue();
            return;
        }
        final int stateId = this.getParamInt(1);
        if (paramCount == 2) {
            this.addReturnValue(info.hasState(stateId));
        }
        else if (paramCount == 3) {
            final int stateLevel = this.getParamInt(2);
            this.addReturnValue(info.hasState(stateId, stateLevel));
        }
        else {
            this.writeError(HasState.m_logger, "Nombre de parametres invalides : " + paramCount);
            this.addReturnNilValue();
        }
    }
    
    @Override
    public String getDescription() {
        return "Renvoie true si le personnage dont l'id pass? en param porte l'?tat d'id et de niveau (optionnel) donn?";
    }
    
    static {
        m_logger = Logger.getLogger((Class)HasState.class);
        HAS_STATE_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("stateId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("stateLevel", null, LuaScriptParameterType.INTEGER, true) };
        HAS_STATE_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("hasState", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
