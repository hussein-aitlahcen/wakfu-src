package com.ankamagames.wakfu.client.core.script.function.context;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import org.keplerproject.luajava.*;

public class IsWalkable extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public IsWalkable(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "isWalkable";
    }
    
    @Override
    public String getDescription() {
        return "Permet de savoir si le perso peut marcher aux coordonn?es donn?es";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("worldX", "Position x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldY", "Position y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldZ", "Position z", LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("walkable", "Si le perso peu marcher ou pas", LuaScriptParameterType.BOOLEAN, false) };
    }
    
    public void run(final int paramCount) throws LuaException {
        final int cellX = this.getParamInt(0);
        final int cellY = this.getParamInt(1);
        final int cellZ = this.getParamInt(2);
        this.addReturnValue(TopologyMapManager.isWalkable(cellX, cellY, (short)cellZ));
    }
    
    static {
        m_logger = Logger.getLogger((Class)IsWalkable.class);
    }
}
