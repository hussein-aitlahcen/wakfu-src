package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetCaster extends SpellEffectActionFunction
{
    private static final String NAME = "getCaster";
    private static final String DESC = "Retourne l'id du caster de l'effet";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetCaster(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
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
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetCaster.RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        this.addReturnValue(this.m_spellEffectAction.getCasterId());
    }
    
    @Override
    public String getDescription() {
        return "Retourne l'id du caster de l'effet";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("casterId", null, LuaScriptParameterType.LONG, false) };
    }
}
