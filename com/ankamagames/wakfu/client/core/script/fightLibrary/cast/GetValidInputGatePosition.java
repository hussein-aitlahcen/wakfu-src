package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import com.ankamagames.wakfu.client.core.action.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetValidInputGatePosition extends CastFunction
{
    private static final String NAME = "getValidInputGatePosition";
    private static final String DESC = "Renvoie la position d'un portail d'entr\u00e9e valide";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetValidInputGatePosition(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState, castAction);
    }
    
    @Nullable
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[0];
    }
    
    @Nullable
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetValidInputGatePosition.RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final BasicEffectArea validInputGate = this.m_castAction.getValidInputGate();
        if (validInputGate == null) {
            this.addReturnNilValue();
            return;
        }
        final Point3 position = validInputGate.getPosition();
        this.addReturnValue(position.getX());
        this.addReturnValue(position.getY());
        this.addReturnValue(position.getZ());
    }
    
    @Override
    public String getName() {
        return "getValidInputGatePosition";
    }
    
    @Override
    public String getDescription() {
        return "Renvoie la position d'un portail d'entr\u00e9e valide";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", null, LuaScriptParameterType.INTEGER, false) };
    }
}
