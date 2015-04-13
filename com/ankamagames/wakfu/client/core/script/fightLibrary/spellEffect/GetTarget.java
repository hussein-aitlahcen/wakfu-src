package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetTarget extends SpellEffectActionFunction
{
    private static final String NAME = "getTarget";
    private static final String DESC = "Retourne l'id de la cible de l'effet";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetTarget(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
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
        this.addReturnValue(this.m_spellEffectAction.getTargetId());
    }
    
    @Override
    public String getDescription() {
        return "Retourne l'id de la cible de l'effet";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("targetId", null, LuaScriptParameterType.LONG, false) };
    }
}
