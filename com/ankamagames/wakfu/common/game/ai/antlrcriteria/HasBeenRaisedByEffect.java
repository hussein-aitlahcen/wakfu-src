package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;

public final class HasBeenRaisedByEffect extends FunctionCriterion
{
    private static final ArrayList<ParserType[]> SIGNATURES;
    private boolean m_useTarget;
    
    public HasBeenRaisedByEffect(final ArrayList<ParserObject> args) {
        super();
        this.checkType(args);
        this.m_useTarget = args.get(0).getValue().equalsIgnoreCase("target");
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return HasBeenRaisedByEffect.SIGNATURES;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final CriterionUser target = CriteriaUtils.getTargetCriterionUserFromParameters(this.m_useTarget, criterionUser, criterionTarget, criterionContext, criterionContent);
        if (target instanceof BasicCharacterInfo) {
            return ((BasicCharacterInfo)target).hasBeenRaisedByTrigger() ? 0 : -1;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.HAS_BEEN_RAISED_BY_EFFECT;
    }
    
    static {
        (SIGNATURES = new ArrayList<ParserType[]>()).add(CriterionConstants.ONE_STRING_SIGNATURE);
    }
}
