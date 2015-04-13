package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public final class IsCompanion extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private String m_targetType;
    
    public IsCompanion(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_targetType = args.get(0).getValue();
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return IsCompanion.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTarget(this.m_targetType, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target == null) {
            return -1;
        }
        return target.is(CriterionUserType.COMPANION) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_COMPANION;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
