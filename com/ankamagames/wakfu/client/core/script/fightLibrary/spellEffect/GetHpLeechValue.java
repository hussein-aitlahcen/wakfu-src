package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetHpLeechValue extends SpellEffectActionFunction
{
    private static final String NAME = "getHpLeechValue";
    private static final String DESC = "Utilisable uniquement avec des effets de vol de vie (11,13,12,14,15) :\n Retourne la valeur de la vie vol?e";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetHpLeechValue(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
    }
    
    @Override
    public String getName() {
        return "getHpLeechValue";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetHpLeechValue.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        int value = 0;
        if (this.m_spellEffectAction.getRunningEffect() instanceof HPLeech) {
            final HPLeech re = (HPLeech)this.m_spellEffectAction.getRunningEffect();
            value = re.getLifeStolen();
        }
        this.addReturnValue(value);
    }
    
    @Override
    public String getDescription() {
        return "Utilisable uniquement avec des effets de vol de vie (11,13,12,14,15) :\n Retourne la valeur de la vie vol?e";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("value", null, LuaScriptParameterType.INTEGER, false) };
    }
}
