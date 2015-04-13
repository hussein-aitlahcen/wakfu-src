package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class IsInAlignment extends FunctionCriterion
{
    private static final List<ParserType[]> SIGNATURES;
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return IsInAlignment.SIGNATURES;
    }
    
    public IsInAlignment(final ArrayList<ParserObject> args) {
        super();
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final AbstractFight fight = CriteriaUtils.getFightFromContext(criterionContext);
        if (fight == null) {
            return -1;
        }
        final Point3 casterPos = CriteriaUtils.getTargetPosition(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (casterPos == null) {
            return -1;
        }
        final Point3 targetPos = CriteriaUtils.getTargetPosition(true, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (targetPos == null) {
            return -1;
        }
        if (casterPos.getX() == targetPos.getX() || casterPos.getY() == targetPos.getY()) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_IN_ALIGNMENT;
    }
    
    static {
        SIGNATURES = Collections.singletonList(ParserType.EMPTY_ARRAY);
    }
}
