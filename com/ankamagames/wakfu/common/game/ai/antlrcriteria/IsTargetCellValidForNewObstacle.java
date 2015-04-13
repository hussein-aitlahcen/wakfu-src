package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.common.game.fight.*;

public final class IsTargetCellValidForNewObstacle extends FunctionCriterion
{
    private static ArrayList<ParserType[]> signatures;
    private static final int[][] CELLS_AROUND;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsTargetCellValidForNewObstacle.signatures;
    }
    
    public IsTargetCellValidForNewObstacle(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    private boolean isCellBlocking(final FightMap fightMap, final int x, final int y) {
        if (fightMap.isBlocked(x, y)) {
            return true;
        }
        final FightObstacle obstacle = fightMap.getObstacle(x, y);
        if (obstacle == null || !obstacle.isBlockingMovement()) {
            return false;
        }
        if (!(obstacle instanceof CriterionUser)) {
            IsTargetCellValidForNewObstacle.m_logger.error((Object)("Unknown obstacle type : " + obstacle));
            return true;
        }
        return !((CriterionUser)obstacle).is(CriterionUserType.CHARACTER);
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionTarget == null) {
            return 0;
        }
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null) {
            return -1;
        }
        Point3 targetPosition;
        if (criterionTarget instanceof Point3) {
            targetPosition = (Point3)criterionTarget;
        }
        else {
            if (!(criterionTarget instanceof CriterionUser)) {
                return -2;
            }
            targetPosition = ((CriterionUser)criterionTarget).getPosition();
        }
        final FightMap fightMap = fight.getFightMap();
        int phaseChanges = 0;
        boolean previousCellWasValid = false;
        final Point3 previousCell = new Point3();
        final Point3 currentCell = new Point3();
        final PathFinder pathFinder = PathFinder.checkOut();
        pathFinder.setMoverCaracteristics(6, (byte)0, (short)4);
        pathFinder.setTopologyMapInstanceSet(fightMap);
        fightMap.setIgnoreAllMovementObstacles(true);
        try {
            for (int i = 0; i <= IsTargetCellValidForNewObstacle.CELLS_AROUND.length; ++i) {
                final int[] offset = IsTargetCellValidForNewObstacle.CELLS_AROUND[i % IsTargetCellValidForNewObstacle.CELLS_AROUND.length];
                final int x = targetPosition.getX() + offset[0];
                final int y = targetPosition.getY() + offset[1];
                final boolean isInMap = fightMap.isInsideOrBorder(x, y);
                final short cellHeight = (short)(isInMap ? fightMap.getCellHeight(x, y) : 0);
                currentCell.set(x, y, cellHeight);
                final boolean cellValid = isInMap && !this.isCellBlocking(fightMap, x, y);
                if (i != 0) {
                    if (cellValid != previousCellWasValid) {
                        ++phaseChanges;
                    }
                    else if (cellValid && !pathFinder.checkMovementOnNextCell(previousCell, currentCell)) {
                        ++phaseChanges;
                    }
                }
                previousCell.set(currentCell);
                previousCellWasValid = cellValid;
            }
        }
        finally {
            fightMap.setIgnoreAllMovementObstacles(false);
            pathFinder.release();
        }
        if (phaseChanges == 0 || phaseChanges == 2) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_TARGET_CELL_VALID_FOR_NEW_OBSTACLE;
    }
    
    static {
        (IsTargetCellValidForNewObstacle.signatures = new ArrayList<ParserType[]>()).add(new ParserType[0]);
        CELLS_AROUND = new int[][] { { -1, -1 }, { 0, -1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 } };
    }
}
