package com.ankamagames.wakfu.client.core.script.function.context;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import org.keplerproject.luajava.*;

public class IsCellBlockedByObstacle extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    public IsCellBlockedByObstacle(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "isCellBlockedByObstacle";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("worldX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldY", null, LuaScriptParameterType.INTEGER, false) };
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("cellBlockedByObstacle", null, LuaScriptParameterType.BOOLEAN, false) };
    }
    
    public void run(final int paramCount) throws LuaException {
        final int cellX = this.getParamInt(0);
        final int cellY = this.getParamInt(1);
        final FightInfo fight = FightManager.getInstance().getFightById(this.getScriptObject().getFightId());
        if (fight == null) {
            this.writeError(IsCellBlockedByObstacle.m_logger, "pas de combat trouv?");
            this.addReturnNilValue();
            return;
        }
        final FightObstacle o = fight.getFightMap().getObstacle(cellX, cellY);
        this.addReturnValue(o != null);
    }
    
    static {
        m_logger = Logger.getLogger((Class)IsCellBlockedByObstacle.class);
    }
}
