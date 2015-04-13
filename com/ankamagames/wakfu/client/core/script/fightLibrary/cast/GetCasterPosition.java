package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetCasterPosition extends CastFunction
{
    private static final Logger m_logger;
    private static final String NAME = "getCasterPosition";
    private static final String DESC = "Permet de r?cup?rer les coordonn?es de la cellule du lanceur de l'action";
    private static final LuaScriptParameterDescriptor[] GET_CASTER_POSITION_RESULTS;
    
    GetCasterPosition(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState, castAction);
    }
    
    @Override
    public String getName() {
        return "getCasterPosition";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetCasterPosition.GET_CASTER_POSITION_RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(this.m_castAction.getInstigatorId());
        if (info == null) {
            this.writeError(GetCasterPosition.m_logger, "CharacterInfo introuvable : " + this.m_castAction.getInstigatorId());
            this.addReturnNilValue();
            return;
        }
        final Point3 position = info.getPosition();
        this.addReturnValue(position.getX());
        this.addReturnValue(position.getY());
        this.addReturnValue(position.getZ());
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer les coordonn?es de la cellule du lanceur de l'action";
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetCasterPosition.class);
        GET_CASTER_POSITION_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", null, LuaScriptParameterType.INTEGER, false) };
    }
}
