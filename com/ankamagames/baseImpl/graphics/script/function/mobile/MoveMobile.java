package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class MoveMobile extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final PathFinderParameters PATHFIND_PARAMETERS;
    private static final TopologyMapInstanceSet PATHFINDER_MAP_INSTANCE_SET;
    private static final String NAME = "moveMobile";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public MoveMobile(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "moveMobile";
    }
    
    @Override
    public String getDescription() {
        return "Lance le d?placement d'un mobile de sa position courante vers une position donn?e, en utilisant l'animation de marche.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return MoveMobile.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final int worldX = this.getParamInt(1);
        final int worldY = this.getParamInt(2);
        final int altitude = this.getParamInt(3);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile != null) {
            if (mobile instanceof PathMobile) {
                final PathMobile pathMobile = (PathMobile)mobile;
                TopologyMapManager.getTopologyMapInstances(mobile.getWorldCellX(), mobile.getWorldCellY(), worldX, worldY, MoveMobile.PATHFINDER_MAP_INSTANCE_SET);
                final PathFinder pathFinder = PathFinder.checkOut();
                MoveMobile.PATHFIND_PARAMETERS.m_limitTo4Directions = (pathMobile.getAvailableDirections() != 8);
                pathFinder.setParameters(MoveMobile.PATHFIND_PARAMETERS);
                pathFinder.setMoverCaracteristics(pathMobile.getHeight(), pathMobile.getPhysicalRadius(), pathMobile.getJumpCapacity());
                pathFinder.setTopologyMapInstanceSet(MoveMobile.PATHFINDER_MAP_INSTANCE_SET);
                pathFinder.addStartCell(mobile.getWorldCellX(), mobile.getWorldCellY(), (short)mobile.getAltitude());
                pathFinder.setStopCell(worldX, worldY, (short)altitude);
                pathFinder.findPath();
                final PathFindResult result = pathFinder.getPathResult();
                MoveMobile.PATHFINDER_MAP_INSTANCE_SET.reset();
                if (result.isPathFound()) {
                    pathMobile.setPath(result, true, true);
                }
                pathFinder.release();
                if (paramCount > 4) {
                    final LuaScript script = this.getScriptObject();
                    final String func = this.getParamString(4);
                    final LuaValue[] params = this.getParams(5, paramCount);
                    pathMobile.addEndPositionListener(new MobileEndPathListener() {
                        @Override
                        public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                            mobile.removeEndPositionListener(this);
                            script.runFunction(func, params, new LuaTable[0]);
                        }
                    });
                }
            }
            else {
                this.writeError(MoveMobile.m_logger, "le mobile " + mobileId + " n'est pas un PAthMobile ");
            }
        }
        else {
            this.writeError(MoveMobile.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)MoveMobile.class);
        PATHFIND_PARAMETERS = new PathFinderParameters();
        MoveMobile.PATHFIND_PARAMETERS.m_limitTo4Directions = true;
        MoveMobile.PATHFIND_PARAMETERS.m_searchLimit = 400;
        MoveMobile.PATHFIND_PARAMETERS.m_stopBeforeEndCell = false;
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("worldX", "Destination x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldY", "Destination y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("altitude", "Destination z", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("func", "Fonction ? appeler une fois le mobile ? destination", LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("params", "Param?tres de la fonction a appeler", LuaScriptParameterType.BLOOPS, true) };
        PATHFINDER_MAP_INSTANCE_SET = new TopologyMapInstanceSet();
    }
}
