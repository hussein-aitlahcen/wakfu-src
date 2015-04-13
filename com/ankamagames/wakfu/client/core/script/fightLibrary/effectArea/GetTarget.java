package com.ankamagames.wakfu.client.core.script.fightLibrary.effectArea;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetTarget extends JavaFunctionEx
{
    private static final String NAME = "getTarget";
    private static final String DESC = "Permet de r?cup?rer l'id du personnage qui a activ? la zone";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    private final EffectAreaTriggeredAction m_effectAreaAction;
    
    GetTarget(final LuaState luaState, final EffectAreaTriggeredAction effectAreaAction) {
        super(luaState);
        this.m_effectAreaAction = effectAreaAction;
    }
    
    @Override
    public String getName() {
        return "getTarget";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetTarget.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        this.addReturnValue(this.m_effectAreaAction.getTargetId());
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer l'id du personnage qui a activ? la zone";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("targetId", null, LuaScriptParameterType.LONG, false) };
    }
}
