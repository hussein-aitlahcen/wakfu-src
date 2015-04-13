package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public final class HasValidPathToTarget extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private static final String IGNORE_OBSTACLES = "ignoreObstacles";
    private boolean m_ignoreObstacles;
    private long m_pathMaxLength;
    
    public HasValidPathToTarget(final ArrayList<ParserObject> args) {
        super();
        final byte sigIndex = this.checkType(args);
        this.m_ignoreObstacles = false;
        this.m_pathMaxLength = -1L;
        switch (sigIndex) {
            case 1: {
                this.m_ignoreObstacles = args.get(0).getValue().equalsIgnoreCase("ignoreObstacles");
                break;
            }
            case 2: {
                this.m_pathMaxLength = args.get(0).getLongValue(null, null, null, null);
                break;
            }
            case 3: {
                this.m_ignoreObstacles = args.get(0).getValue().equalsIgnoreCase("ignoreObstacles");
                this.m_pathMaxLength = args.get(1).getLongValue(null, null, null, null);
                break;
            }
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasValidPathToTarget.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (criterionTarget == null) {
            return 0;
        }
        final BasicCharacterInfo caster = CriteriaUtils.getTargetCharacterInfoFromParameters(criterionUser, criterionTarget, criterionContext, criterionContent);
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
        if (this.m_ignoreObstacles) {
            fightMap.setIgnoreAllMovementObstacles(true);
        }
        final PathFinder pathFinder = PathFinder.checkOut();
        int path = -1;
        try {
            pathFinder.setMoverCaracteristics(caster.getHeight(), caster.getPhysicalRadius(), caster.getJumpCapacity());
            pathFinder.setTopologyMapInstanceSet(fightMap);
            pathFinder.addStartCell(caster.getPosition());
            pathFinder.setStopCell(targetPosition);
            final PathFinderParameters parameters = new PathFinderParameters();
            parameters.m_maxPathLength = (int)this.m_pathMaxLength;
            parameters.m_searchLimit = 256;
            pathFinder.setParameters(parameters);
            path = pathFinder.findPath();
        }
        catch (Exception e) {
            HasValidPathToTarget.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        finally {
            pathFinder.release();
            fightMap.setIgnoreAllMovementObstacles(false);
        }
        if (path == -1) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_VALID_PATH_TO_TARGET;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        HasValidPathToTarget.SIGNATURES.add(CriterionConstants.ONE_STRING_SIGNATURE);
        HasValidPathToTarget.SIGNATURES.add(CriterionConstants.ONE_NUMBER_SIGNATURE);
        HasValidPathToTarget.SIGNATURES.add(new ParserType[] { ParserType.STRING, ParserType.NUMBER });
    }
}
