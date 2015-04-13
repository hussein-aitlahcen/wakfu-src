package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetBarrierLossValue extends SpellEffectActionFunction
{
    private static final String NAME = "getBarrierLossValue";
    private static final String DESC = "Retourne la valeur de barri\u00e8re perdue";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetBarrierLossValue(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
    }
    
    @Override
    public String getName() {
        return "getBarrierLossValue";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetBarrierLossValue.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        this.addReturnValue(this.m_spellEffectAction.getBarrierLossValue());
    }
    
    @Override
    public String getDescription() {
        return "Retourne la valeur de barri\u00e8re perdue";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("value", null, LuaScriptParameterType.INTEGER, false) };
    }
}
