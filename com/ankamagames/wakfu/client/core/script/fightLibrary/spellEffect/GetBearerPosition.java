package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetBearerPosition extends SpellEffectActionFunction
{
    private static final String NAME = "getBearerPosition";
    private static final String DESC = "Retourne les coordonn?es de la position du porteur de l'effet (le propri?taire du manager d'effet dans lequel est stock? l'effet)";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetBearerPosition(final LuaState luaState, final SpellEffectActionInterface spellEffectAction) {
        super(luaState, spellEffectAction);
    }
    
    @Override
    public String getName() {
        return "getBearerPosition";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetBearerPosition.RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final Point3 position = this.m_spellEffectAction.getBearerPosition();
        if (position == null) {
            return;
        }
        this.addReturnValue(position.getX());
        this.addReturnValue(position.getY());
        this.addReturnValue(position.getZ());
    }
    
    @Override
    public String getDescription() {
        return "Retourne les coordonn?es de la position du porteur de l'effet (le propri?taire du manager d'effet dans lequel est stock? l'effet)";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", null, LuaScriptParameterType.INTEGER, false) };
    }
}
