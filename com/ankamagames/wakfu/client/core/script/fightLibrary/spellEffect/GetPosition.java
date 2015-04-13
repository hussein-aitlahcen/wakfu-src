package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetPosition extends SpellEffectActionFunction
{
    private static final String NAME = "getPosition";
    private static final String DESC = "Retourne les coordonn?es de la position de la cible de l'effet si elle existe, sinon retourne celles de la cellule cible";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetPosition(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
    }
    
    @Override
    public String getName() {
        return "getPosition";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetPosition.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        final Point3 position = this.m_spellEffectAction.getPosition();
        this.addReturnValue(position.getX());
        this.addReturnValue(position.getY());
        this.addReturnValue(position.getZ());
    }
    
    @Override
    public String getDescription() {
        return "Retourne les coordonn?es de la position de la cible de l'effet si elle existe, sinon retourne celles de la cellule cible";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", null, LuaScriptParameterType.INTEGER, false) };
    }
}
