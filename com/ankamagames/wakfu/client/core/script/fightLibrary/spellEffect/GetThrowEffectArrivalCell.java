package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.script.*;

final class GetThrowEffectArrivalCell extends SpellEffectActionFunction
{
    private static final String NAME = "getThrowEffectArrivalCell";
    private static final String DESC = "Utilisable uniquement sur l'effet 333 \"Jette le personnage port?\" :\n retourne les coordonn?es de la position d'arriv?e du lancer";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetThrowEffectArrivalCell(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
    }
    
    @Override
    public String getName() {
        return "getThrowEffectArrivalCell";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetThrowEffectArrivalCell.RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final WakfuRunningEffect effect = this.m_spellEffectAction.getRunningEffect();
        if (effect == null) {
            throw new LuaException("On ne peut pas utiliser la fonction getMovementEffectArrivalCell sur un effet qui est null");
        }
        if (!(effect instanceof Throw)) {
            throw new LuaException("On ne peut pas utiliser la fonction getMovementEffectArrivalCell sur un effet de type " + effect.getClass().getSimpleName());
        }
        final Point3 position = ((Throw)effect).getArrivalCell();
        this.addReturnValue(position.getX());
        this.addReturnValue(position.getY());
        this.addReturnValue(position.getZ());
    }
    
    @Override
    public String getDescription() {
        return "Utilisable uniquement sur l'effet 333 \"Jette le personnage port?\" :\n retourne les coordonn?es de la position d'arriv?e du lancer";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", null, LuaScriptParameterType.INTEGER, false) };
    }
}
