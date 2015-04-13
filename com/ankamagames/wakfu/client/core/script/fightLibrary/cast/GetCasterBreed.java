package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetCasterBreed extends CastFunction
{
    private static final Logger m_logger;
    private static final String NAME = "getCasterBreed";
    private static final String DESC = "Permet de r?cup?rer l'id de la breed du lanceur";
    private static final LuaScriptParameterDescriptor[] GET_CASTER_BREED_RESULTS;
    
    GetCasterBreed(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState, castAction);
    }
    
    @Override
    public String getName() {
        return "getCasterBreed";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetCasterBreed.GET_CASTER_BREED_RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(this.m_castAction.getInstigatorId());
        if (info == null) {
            this.writeError(GetCasterBreed.m_logger, "CharacterInfo introuvable : " + this.m_castAction.getInstigatorId());
            this.addReturnNilValue();
            return;
        }
        this.addReturnValue(info.getBreedId());
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer l'id de la breed du lanceur";
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetCasterBreed.class);
        GET_CASTER_BREED_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("breedId", null, LuaScriptParameterType.INTEGER, false) };
    }
}
