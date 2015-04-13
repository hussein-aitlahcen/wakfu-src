package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class HasLineOfSight extends FunctionCriterion
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasLineOfSight.SIGNATURES;
    }
    
    public HasLineOfSight(final ArrayList<ParserObject> args) {
        super();
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
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        final Point3 targetPos = CriteriaUtils.getTargetPosition(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target != null) {
            if (FightFunctions.hasLineOfSight(source, fight.getFightMap(), targetPos.getX(), targetPos.getY(), targetPos.getZ(), target)) {
                return 0;
            }
        }
        else if (FightFunctions.hasLineOfSight(source, fight, targetPos.getX(), targetPos.getY(), targetPos.getZ())) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HASLINEOFSIGHT;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
