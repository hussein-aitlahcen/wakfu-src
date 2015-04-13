package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public final class IsInPlay extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private boolean m_useTarget;
    
    public IsInPlay(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        if (args.size() >= 1) {
            this.m_useTarget = args.get(0).getValue().equalsIgnoreCase("target");
        }
    }
    
    @Override
    protected ArrayList<ParserType[]> getSignatures() {
        return IsInPlay.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        return (target != null && target.isInPlay()) ? 0 : -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_IN_PLAY;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.EMPTY_SIGNATURE);
        IsInPlay.SIGNATURES.add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
