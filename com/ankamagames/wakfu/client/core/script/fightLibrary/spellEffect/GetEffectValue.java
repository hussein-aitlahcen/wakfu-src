package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetEffectValue extends SpellEffectActionFunction
{
    private static final String NAME = "getEffectValue";
    private static final String DESC = "Retourne la valeur de l'effet";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetEffectValue(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
    }
    
    @Override
    public String getName() {
        return "getEffectValue";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetEffectValue.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        this.addReturnValue(this.m_spellEffectAction.getEffectValue());
    }
    
    @Override
    public String getDescription() {
        return "Retourne la valeur de l'effet";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("value", null, LuaScriptParameterType.INTEGER, false) };
    }
}
