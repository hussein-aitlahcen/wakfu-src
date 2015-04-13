package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import com.ankamagames.wakfu.client.core.action.*;
import org.jetbrains.annotations.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class IsTargetCellInRange extends CastFunction
{
    private static final String NAME = "isTargetCellInRange";
    private static final String DESC = "Renvoie true si la cellule cibl\u00e9e et situer dans la zone de port\u00e9e du sort";
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    IsTargetCellInRange(final LuaState luaState, final AbstractFightCastAction castAction) {
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
        return IsTargetCellInRange.RESULTS;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        this.addReturnValue(this.m_castAction.isTargetCellInRange());
    }
    
    @Override
    public String getName() {
        return "isTargetCellInRange";
    }
    
    @Override
    public String getDescription() {
        return "Renvoie true si la cellule cibl\u00e9e et situer dans la zone de port\u00e9e du sort";
    }
    
    static {
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("targetCellInRange", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
