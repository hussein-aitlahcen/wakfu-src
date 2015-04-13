package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;

abstract class SpellEffectActionFunction extends JavaFunctionEx
{
    protected final SpellEffectActionInterface m_spellEffectAction;
    
    public SpellEffectActionFunction(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState);
        this.m_spellEffectAction = spellEffectAction;
    }
}
