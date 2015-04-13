package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

class GetCaster extends CastFunction
{
    private static final String NAME = "getCaster";
    private static final String DESC = "Permet de r?cup?rer l'id du lanceur de l'action";
    private static final LuaScriptParameterDescriptor[] GET_CASTER_RESULTS;
    
    GetCaster(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState, castAction);
    }
    
    @Override
    public String getName() {
        return "getCaster";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetCaster.GET_CASTER_RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        this.addReturnValue(this.m_castAction.getInstigatorId());
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer l'id du lanceur de l'action";
    }
    
    static {
        GET_CASTER_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("casterId", null, LuaScriptParameterType.LONG, false) };
    }
}
