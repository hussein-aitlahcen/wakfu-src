package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public class GetBooleanValue extends FunctionValue
{
    private static ArrayList<ParserType[]> signatures;
    private SimpleCriterion m_criterion;
    
    public GetBooleanValue(final ArrayList<ParserObject> args) {
        super();
        if (this.checkType(args) == 0) {
            this.m_criterion = args.get(0);
        }
    }
    
    @Override
    protected List<ParserType[]> getSignatures() {
        return GetBooleanValue.signatures;
    }
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        return (this.m_criterion != null && this.m_criterion.getValidity(criterionUser, criterionTarget, criterionContent, criterionContext) == 0) ? 1 : 0;
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.GET_BOOLEAN_VALUE;
    }
    
    static {
        GetBooleanValue.signatures = new ArrayList<ParserType[]>();
        final ParserType[] sig = { ParserType.BOOLEAN };
        GetBooleanValue.signatures.add(sig);
    }
}
