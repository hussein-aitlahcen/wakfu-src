package com.ankamagames.wakfu.common.game.fight;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public class WakfuFightMap extends FightMap
{
    private static final Logger m_logger;
    
    @Override
    protected int getObstacleCellIndex(final FightObstacle obstacle) {
        if (obstacle == null) {
            return -1;
        }
        final byte obstacleId = obstacle.getObstacleId();
        if (!this.m_obstaclesPosition.contains(obstacleId)) {
            WakfuFightMap.m_logger.warn((Object)("On ne retrouve pas l'index de la cellule ou est situ\u00e9 l'obstacle " + obstacleId));
            return -1;
        }
        return this.m_obstaclesPosition.getLastKnownCellIndex(obstacleId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuFightMap.class);
    }
}
