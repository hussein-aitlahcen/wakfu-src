package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public final class HasLineOfSightToEnemy extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private boolean m_checkDirection;
    
    public HasLineOfSightToEnemy(final ArrayList<ParserObject> args) {
        super();
        if (args.size() >= 1) {
            this.m_checkDirection = args.get(0).isValid(null, null, null, null);
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasLineOfSightToEnemy.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser source = CriteriaUtils.getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (source == null) {
            return -1;
        }
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null) {
            return -1;
        }
        final Point3 criterionTargetPos = CriteriaUtils.getTargetPosition(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        final Point3 sourcePosition = CriteriaUtils.getTargetPosition(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        final Direction8 directionToCriterionTarget = sourcePosition.getDirection4To(criterionTargetPos);
        final Collection enemies = fight.getFightersInPlayNotInTeam(source.getTeamId());
        for (final Object enemy : enemies) {
            final BasicCharacterInfo dest = (BasicCharacterInfo)enemy;
            final Point3 targetPos = dest.getPositionConst();
            if (this.m_checkDirection && directionToCriterionTarget != null && !directionToCriterionTarget.equals(sourcePosition.getDirection4To(targetPos))) {
                continue;
            }
            if (FightFunctions.hasLineOfSight(source, fight.getFightMap(), targetPos.getX(), targetPos.getY(), targetPos.getZ(), dest)) {
                return 0;
            }
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_LINE_OF_SIGHT_TO_ENEMY;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(new ParserType[0]);
        HasLineOfSightToEnemy.SIGNATURES.add(new ParserType[] { ParserType.BOOLEAN });
    }
}
