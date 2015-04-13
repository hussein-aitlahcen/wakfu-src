package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetLevel extends CastFunction
{
    private static final String NAME = "getLevel";
    private static final String DESC = "Permet de r?cup?rer le niveau du sort ou du skill associ? ? l'action";
    private static final LuaScriptParameterDescriptor[] GET_CAST_LEVEL_RESULTS;
    
    GetLevel(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState, castAction);
    }
    
    @Override
    public String getName() {
        return "getLevel";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetLevel.GET_CAST_LEVEL_RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        this.addReturnValue(this.m_castAction.getLevel());
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer le niveau du sort ou du skill associ? ? l'action";
    }
    
    static {
        GET_CAST_LEVEL_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("castLevel", null, LuaScriptParameterType.INTEGER, false) };
    }
}
