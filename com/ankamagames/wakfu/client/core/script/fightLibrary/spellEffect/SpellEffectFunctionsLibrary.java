package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SpellEffectFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final String NAME = "SpellEffect";
    private static final String DESC = "Librairie ? utiliser dans les scripts d'effets uniquement";
    private final SpellEffectActionInterface m_spellEffectAction;
    
    public SpellEffectFunctionsLibrary(final SpellEffectActionInterface action) {
        super();
        this.m_spellEffectAction = action;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new RollEcaflipDice(luaState, this.m_spellEffectAction), new GetTarget(luaState, this.m_spellEffectAction), new DisplayFlyingValue(luaState, this.m_spellEffectAction), new GetEffectValue(luaState, this.m_spellEffectAction), new GetHpLeechValue(luaState, this.m_spellEffectAction), new GetPosition(luaState, this.m_spellEffectAction), new GetTargetCell(luaState, this.m_spellEffectAction), new GetCaster(luaState, this.m_spellEffectAction), new GetMovementEffectArrivalCell(luaState, this.m_spellEffectAction), new GetThrowEffectArrivalCell(luaState, this.m_spellEffectAction), new GetMover(luaState, this.m_spellEffectAction), new DisplayFlyingItem(luaState, this.m_spellEffectAction), new GetEffectArea(luaState, this.m_spellEffectAction), new GetCasterPosition(luaState, this.m_spellEffectAction), new GetBearerPosition(luaState, this.m_spellEffectAction), new SetEffectAreaPlayDeathAnim(luaState), new HasState(luaState), new GetEffectAreaBaseId(luaState), new GetArmorLossValue(luaState, this.m_spellEffectAction), new GetBarrierLossValue(luaState, this.m_spellEffectAction) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public final String getName() {
        return "SpellEffect";
    }
    
    @Override
    public String getDescription() {
        return "Librairie ? utiliser dans les scripts d'effets uniquement";
    }
}
