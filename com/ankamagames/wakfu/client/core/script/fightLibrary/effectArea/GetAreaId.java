package com.ankamagames.wakfu.client.core.script.fightLibrary.effectArea;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetAreaId extends JavaFunctionEx
{
    private static final String NAME = "getAreaId";
    private static final String DESC = "Permet de r?cup?rer l'id de la zone associ?e de l'action";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    private final EffectAreaTriggeredAction m_effectAreaAction;
    
    GetAreaId(final LuaState luaState, final EffectAreaTriggeredAction effectAreaAction) {
        super(luaState);
        this.m_effectAreaAction = effectAreaAction;
    }
    
    @Override
    public String getName() {
        return "getAreaId";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetAreaId.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        this.addReturnValue(this.m_effectAreaAction.getInstigatorId());
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer l'id de la zone associ?e de l'action";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("areaId", null, LuaScriptParameterType.LONG, false) };
    }
}
