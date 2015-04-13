package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public final class IsCellBehindTargetFree extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    public IsCellBehindTargetFree(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsCellBehindTargetFree.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (criterionTarget == null) {
            return -1;
        }
        final Point3 targetPos = new Point3(target.getPosition());
        final Direction8 direction = target.getDirection();
        targetPos.sub(direction.m_x, direction.m_y, 0);
        final SimpleCriterion isTargetCellFree = new IsTargetCellFree();
        return isTargetCellFree.getValidity(criterionUser, targetPos, criterionContent, criterionContext);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_CELL_BEHIND_TARGET_FREE;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
