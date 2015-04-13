package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetArmorLossValue extends SpellEffectActionFunction
{
    private static final String NAME = "getArmorLossValue";
    private static final String DESC = "Retourne la valeur d'armure perdue";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetArmorLossValue(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
    }
    
    @Override
    public String getName() {
        return "getArmorLossValue";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetArmorLossValue.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        this.addReturnValue(this.m_spellEffectAction.getArmorLossValue());
    }
    
    @Override
    public String getDescription() {
        return "Retourne la valeur d'armure perdue";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("value", null, LuaScriptParameterType.INTEGER, false) };
    }
}
