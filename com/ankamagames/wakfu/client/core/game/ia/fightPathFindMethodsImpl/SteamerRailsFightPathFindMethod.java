package com.ankamagames.wakfu.client.core.game.ia.fightPathFindMethodsImpl;

import com.ankamagames.wakfu.client.core.game.ia.*;
import org.apache.log4j.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fight.microbotCombination.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.graphics.alea.utils.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.client.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class SteamerRailsFightPathFindMethod implements FightPathFindMethod
{
    public static final Logger m_logger;
    public static final FightPathFindMethod INSTANCE;
    private final SteamerRailsPathFinder m_railsPathFinder;
    protected final PathFinderParameters m_defaultParameters;
    private final ClientPathFindResult EMPTY_PATH;
    
    protected SteamerRailsFightPathFindMethod() {
        super();
        this.m_railsPathFinder = new SteamerRailsPathFinder();
        this.m_defaultParameters = new PathFinderParameters();
        this.EMPTY_PATH = new ClientPathFindResult(PathFindResult.EMPTY, false);
        this.m_defaultParameters.m_searchLimit = 1000;
        this.m_defaultParameters.m_includeStartCell = false;
        this.m_defaultParameters.m_maxPathLength = -1;
        this.m_defaultParameters.m_punishJump = true;
        this.m_defaultParameters.m_punishDirectionChangeIn4D = true;
    }
    
    @Override
    public ClientPathFindResult findPath(final CharacterInfo currentFighter, final CharacterActor actor, final int availableMovementPoints) {
        if (availableMovementPoints <= 0) {
            return TackleAwareFightPathFindMethod.INSTANCE.findPath(currentFighter, actor, availableMovementPoints);
        }
        if (currentFighter.getLinkedCharacter() != null) {
            return TackleAwareFightPathFindMethod.INSTANCE.findPath(currentFighter, actor, availableMovementPoints);
        }
        final boolean railsOnly = currentFighter.hasProperty(FightPropertyType.MOVEMENT_ON_RAILS_ONLY);
        final Fight currentFight = currentFighter.getCurrentFight();
        final List<MicrobotSet> microbotSets = currentFight.getMicrobotSetForTeam(currentFighter.getTeamId());
        if (microbotSets == null || microbotSets.isEmpty() || !MicrobotSet.isPositionInMicrobotSets(currentFighter.getPosition(), microbotSets)) {
            if (railsOnly) {
                return this.EMPTY_PATH;
            }
            return TackleAwareFightPathFindMethod.INSTANCE.findPath(currentFighter, actor, availableMovementPoints);
        }
        else {
            final Point3 destRailPos = this.getRailUnderMouseExactPos(currentFighter, actor, microbotSets);
            if (destRailPos == null) {
                if (railsOnly) {
                    return this.EMPTY_PATH;
                }
                return TackleAwareFightPathFindMethod.INSTANCE.findPath(currentFighter, actor, availableMovementPoints);
            }
            else {
                final FightMap fightMap = currentFight.getFightMap();
                fightMap.addIgnoredMovementObstacle(currentFighter);
                PathFindResult pathResult = null;
                try {
                    this.m_railsPathFinder.initialize(currentFight, currentFighter.getTeamId(), microbotSets);
                    pathResult = WorldSceneInteractionUtils.getPathSolutionFromMouseCoordinatesWithSpecificPathFinder(actor, UIFightFrame.getLastTarget(), this.m_defaultParameters, fightMap, this.m_railsPathFinder);
                }
                finally {
                    fightMap.clearIgnoredMovementObstacles();
                }
                if (pathResult != null && pathResult.isPathFound()) {
                    final boolean oneMpMovement = currentFighter.isActiveProperty(FightPropertyType.MOVEMENT_ON_RAILS_ALWAYS_COST_1_PM);
                    if (oneMpMovement && availableMovementPoints > 0) {
                        return new ClientPathFindResult(pathResult, true);
                    }
                    final int maxLength = availableMovementPoints * 3;
                    if (pathResult.getPathLength() <= maxLength) {
                        return new ClientPathFindResult(pathResult, true);
                    }
                }
                if (railsOnly) {
                    return this.EMPTY_PATH;
                }
                return TackleAwareFightPathFindMethod.INSTANCE.findPath(currentFighter, actor, availableMovementPoints);
            }
        }
    }
    
    private Point3 getRailUnderMouseExactPos(final CharacterInfo currentFighter, final CharacterActor actor, final List<MicrobotSet> microbotSets) {
        final float offsetX = 0.0f;
        final float offsetY = 0.0f;
        final AleaWorldSceneWithParallax worldScene = WakfuClientInstance.getInstance().getWorldScene();
        if (worldScene == null) {
            return null;
        }
        final ArrayList<DisplayedScreenElement> hitElements = worldScene.getDisplayedElementsUnderMousePoint(UIFightFrame.m_mouseX, UIFightFrame.m_mouseY, actor.getAltitude(), DisplayedScreenElementComparator.Z_ORDER, 0.0f, 0.0f);
        final int numElements = hitElements.size();
        if (numElements == 0) {
            return null;
        }
        final Point3 fighterPos = currentFighter.getPositionConst();
        final TLongHashSet alreadyDone = new TLongHashSet(50);
        for (int displayElementIndex = 0; displayElementIndex < numElements; ++displayElementIndex) {
            final DisplayedScreenElement displayedElement = hitElements.get(displayElementIndex);
            final ScreenElement element = displayedElement.getElement();
            if (alreadyDone.add(displayedElement.getLayerReference())) {
                if (!fighterPos.equals(element.getCoordinates())) {
                    if (MicrobotSet.isPositionInMicrobotSets(element.getCoordinates(), microbotSets)) {
                        return new Point3(element.getCoordinates());
                    }
                }
            }
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SteamerRailsFightPathFindMethod.class);
        INSTANCE = new SteamerRailsFightPathFindMethod();
    }
}
