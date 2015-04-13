package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class HasState extends CastFunction
{
    private static final Logger m_logger;
    private static final String NAME = "hasState";
    private static final String DESC = "Permet de savoir si le lanceur de l'action poss?de un ?tat optionnellement d'un certain niveau";
    private static final LuaScriptParameterDescriptor[] HAS_STATE_PARAMS;
    private static final LuaScriptParameterDescriptor[] HAS_STATE_RESULTS;
    
    HasState(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState, castAction);
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
        final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(this.m_castAction.getInstigatorId());
        if (info == null) {
            this.writeError(HasState.m_logger, "CharacterInfo introuvable : " + this.m_castAction.getInstigatorId());
            this.addReturnNilValue();
            return;
        }
        final int stateId = this.getParamInt(0);
        if (paramCount == 1) {
            this.addReturnValue(info.hasState(stateId));
        }
        else if (paramCount == 2) {
            final int stateLevel = this.getParamInt(1);
            this.addReturnValue(info.hasState(stateId, stateLevel));
        }
        else {
            this.writeError(HasState.m_logger, "Nombre de parametres invalides : " + paramCount);
            this.addReturnNilValue();
        }
    }
    
    @Override
    public String getDescription() {
        return "Permet de savoir si le lanceur de l'action poss?de un ?tat optionnellement d'un certain niveau";
    }
    
    static {
        m_logger = Logger.getLogger((Class)HasState.class);
        HAS_STATE_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("stateId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("stateLevel", null, LuaScriptParameterType.INTEGER, true) };
        HAS_STATE_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("hasState", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
