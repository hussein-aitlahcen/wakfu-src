package com.ankamagames.wakfu.client.core.game.ia.fightPathFindMethodsImpl;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.ia.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.graphics.alea.utils.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;

public abstract class AbstractFightPathFindMethod implements FightPathFindMethod
{
    public static final Logger m_logger;
    protected final TackleAwarePathFinder m_tackleAwarePathFinder;
    protected final PathFinderParameters m_defaultParameters;
    
    public AbstractFightPathFindMethod() {
        super();
        this.m_tackleAwarePathFinder = new TackleAwarePathFinder();
        this.m_defaultParameters = new PathFinderParameters();
        this.m_defaultParameters.m_searchLimit = 1000;
        this.m_defaultParameters.m_includeStartCell = false;
    }
    
    protected final PathFindResult computePath(final FightMap fightMap, final CharacterInfo currentFighter, final CharacterActor actor, final int availableMovementPoints, final float ennemyTackleCost, final float battleGroundBorderCost) {
        fightMap.addIgnoredMovementObstacle(currentFighter);
        this.m_defaultParameters.m_maxPathLength = availableMovementPoints;
        this.m_tackleAwarePathFinder.initialize(currentFighter.getCurrentFight(), currentFighter.getTeamId());
        this.m_tackleAwarePathFinder.setEnemyTackleCost(ennemyTackleCost);
        this.m_tackleAwarePathFinder.setNegativeZoneCost(battleGroundBorderCost);
        final PathFindResult pathResult = WorldSceneInteractionUtils.getPathSolutionFromMouseCoordinatesWithSpecificPathFinder(actor, UIFightFrame.getLastTarget(), this.m_defaultParameters, fightMap, this.m_tackleAwarePathFinder);
        this.m_tackleAwarePathFinder.reset();
        fightMap.clearIgnoredMovementObstacles();
        return pathResult;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractFightPathFindMethod.class);
    }
}
