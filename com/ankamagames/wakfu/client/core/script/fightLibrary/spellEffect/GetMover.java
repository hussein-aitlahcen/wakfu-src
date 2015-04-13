package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect.*;
import com.ankamagames.framework.script.*;

final class GetMover extends SpellEffectActionFunction
{
    private static final String NAME = "getMover";
    private static final String DESC = "Utilisable uniquement sur des effets de mouvement (pousser, tirer...) : \nRetourne l'id du personnage d?plac?";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetMover(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
    }
    
    @Override
    public String getName() {
        return "getMover";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetMover.RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final WakfuRunningEffect effect = this.m_spellEffectAction.getRunningEffect();
        if (effect == null) {
            throw new LuaException("On ne peut pas utiliser la fonction getMovementEffectArrivalCell sur un effet qui est null");
        }
        if (!(effect instanceof MovementEffect)) {
            throw new LuaException("On ne peut pas utiliser la fonction getMovementEffectArrivalCell sur un effet de type " + effect.getClass().getSimpleName());
        }
        final MovementEffectUser mover = ((MovementEffect)effect).getMover();
        if (mover != null) {
            this.addReturnValue(mover.getId());
        }
        else {
            this.addReturnValue(-1L);
        }
    }
    
    @Override
    public String getDescription() {
        return "Utilisable uniquement sur des effets de mouvement (pousser, tirer...) : \nRetourne l'id du personnage d?plac?";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("moverId", null, LuaScriptParameterType.LONG, false) };
    }
}
