package com.ankamagames.wakfu.client.core.script.function.context;

import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import org.keplerproject.luajava.*;

public final class GetZ extends JavaFunctionEx
{
    public GetZ(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getZ";
    }
    
    @Override
    public String getDescription() {
        return "Retourne les altitudes walkable d'une position donn?e (dans l'ordre d?croissant)";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("worldX", "Position x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldY", "Position y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldZ", "Position z", LuaScriptParameterType.INTEGER, true) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("worldZ", "Liste des altitudes walkables", LuaScriptParameterType.BLOOPS, true) };
    }
    
    public void run(final int paramCount) throws LuaException {
        final int cellX = this.getParamInt(0);
        final int cellY = this.getParamInt(1);
        if (paramCount == 3) {
            final int cellZ = this.getParamInt(2);
            if (cellZ == -32768) {
                this.addReturnValue(cellZ);
            }
            else {
                this.addReturnValue(TopologyMapManager.getNearestWalkableZ(cellX, cellY, (short)cellZ));
            }
            return;
        }
        final short[] zs = TopologyMapManager.getWalkableZ(cellX, cellY);
        for (int i = 0; i < zs.length; ++i) {
            this.addReturnValue(zs[i]);
        }
    }
}
