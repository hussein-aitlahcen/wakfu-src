package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetPosition extends CastFunction
{
    private static final String NAME = "getPosition";
    private static final String DESC = "Permet de r?cup?rer les coordonn?es de la cellule cibl?e par l'action";
    private static final LuaScriptParameterDescriptor[] GET_CAST_POSITION_RESULTS;
    
    GetPosition(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState, castAction);
    }
    
    @Override
    public String getName() {
        return "getPosition";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetPosition.GET_CAST_POSITION_RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        this.addReturnValue(this.m_castAction.getX());
        this.addReturnValue(this.m_castAction.getY());
        this.addReturnValue(this.m_castAction.getZ());
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer les coordonn?es de la cellule cibl?e par l'action";
    }
    
    static {
        GET_CAST_POSITION_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", null, LuaScriptParameterType.INTEGER, false) };
    }
}
