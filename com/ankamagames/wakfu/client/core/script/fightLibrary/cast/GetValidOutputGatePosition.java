package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import com.ankamagames.wakfu.client.core.action.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetValidOutputGatePosition extends CastFunction
{
    private static final String NAME = "getValidOutputGatePosition";
    private static final String DESC = "Renvoie la position d'un portail de sortie valide";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetValidOutputGatePosition(final LuaState luaState, final AbstractFightCastAction castAction) {
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
        return GetValidOutputGatePosition.RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final BasicEffectArea validOutputGate = this.m_castAction.getValidOutputGate();
        if (validOutputGate == null) {
            this.addReturnNilValue();
            return;
        }
        final Point3 position = validOutputGate.getPosition();
        this.addReturnValue(position.getX());
        this.addReturnValue(position.getY());
        this.addReturnValue(position.getZ());
    }
    
    @Override
    public String getName() {
        return "getValidOutputGatePosition";
    }
    
    @Override
    public String getDescription() {
        return "Renvoie la position d'un portail de sortie valide";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", null, LuaScriptParameterType.INTEGER, false) };
    }
}
