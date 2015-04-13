package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class HasEffect extends CastFunction
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] HAS_EFFECT_PARAMS;
    private static final LuaScriptParameterDescriptor[] HAS_EFFECT_RESULTS;
    private static final String NAME = "hasEffect";
    private static final String DESC = "Permet de savoir si le lanceur de l'action poss?de un effet d'id donn?";
    
    HasEffect(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState, castAction);
    }
    
    @Override
    public String getName() {
        return "hasEffect";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return HasEffect.HAS_EFFECT_PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return HasEffect.HAS_EFFECT_RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(this.m_castAction.getInstigatorId());
        if (info == null) {
            this.writeError(HasEffect.m_logger, "CharacterInfo introuvable : " + this.m_castAction.getInstigatorId());
            this.addReturnNilValue();
            return;
        }
        final int effectId = this.getParamInt(0);
        if (paramCount == 1) {
            this.addReturnValue(info.hasEffectWithReferenceId(effectId));
        }
        else {
            this.writeError(HasEffect.m_logger, "Nombre de parametres invalides : " + paramCount);
            this.addReturnNilValue();
        }
    }
    
    @Override
    public String getDescription() {
        return "Permet de savoir si le lanceur de l'action poss?de un effet d'id donn?";
    }
    
    static {
        m_logger = Logger.getLogger((Class)HasEffect.class);
        HAS_EFFECT_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("effectId", null, LuaScriptParameterType.INTEGER, false) };
        HAS_EFFECT_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("hasEffect", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
