package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;

public final class GetY extends FunctionValue
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private boolean m_useTarget;
    
    public GetY(final ArrayList<ParserObject> args) {
        super();
        final byte sigIdx = this.checkType(args);
        if (sigIdx == 1) {
            this.m_useTarget = args.get(0).getValue().equalsIgnoreCase("target");
        }
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return GetY.SIGNATURES;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (this.m_useTarget && criterionTarget instanceof Point3) {
            return ((Point3)criterionTarget).getY();
        }
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return Long.MIN_VALUE;
        }
        return target.getWorldCellY();
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_Y;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        GetY.SIGNATURES.add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
