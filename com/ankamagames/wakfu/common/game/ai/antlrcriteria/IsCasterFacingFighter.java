package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public final class IsCasterFacingFighter extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    
    public IsCasterFacingFighter(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return IsCasterFacingFighter.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser caster = CriteriaUtils.getTargetCriterionUserFromParameters(false, criterionUser, criterionTarget, criterionContext, criterionContent);
        final Direction8 direction = caster.getDirection();
        final Point3 facingCell = new Point3(caster.getPosition());
        facingCell.shift(direction);
        final CriterionUser target = CriteriaUtils.getTarget("target", null, facingCell, criterionContext, criterionContent);
        return (target instanceof BasicCharacterInfo) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_CASTER_FACING_FIGHTER;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
    }
}
