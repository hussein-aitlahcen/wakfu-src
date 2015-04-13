package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetCasterSex extends CastFunction
{
    private static final Logger m_logger;
    private static final String NAME = "getCasterSex";
    private static final String DESC = "Permet de r?cup?rer l'id du sexe du lanceur de l'action";
    private static final LuaScriptParameterDescriptor[] GET_CASTER_SEX_RESULTS;
    
    GetCasterSex(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState, castAction);
    }
    
    @Override
    public String getName() {
        return "getCasterSex";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetCasterSex.GET_CASTER_SEX_RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(this.m_castAction.getInstigatorId());
        if (info == null) {
            this.writeError(GetCasterSex.m_logger, "CharacterInfo introuvable : " + this.m_castAction.getInstigatorId());
            this.addReturnNilValue();
            return;
        }
        this.addReturnValue(info.getSex());
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer l'id du sexe du lanceur de l'action";
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetCasterSex.class);
        GET_CASTER_SEX_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("sex", null, LuaScriptParameterType.INTEGER, false) };
    }
}
