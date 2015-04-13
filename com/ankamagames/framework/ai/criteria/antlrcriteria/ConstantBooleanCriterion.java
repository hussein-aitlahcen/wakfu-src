package com.ankamagames.framework.ai.criteria.antlrcriteria;

public class ConstantBooleanCriterion extends SimpleCriterion
{
    public static final ConstantBooleanCriterion TRUE;
    public static final ConstantBooleanCriterion FALSE;
    private final boolean m_value;
    
    @Override
    public boolean isConstant() {
        return true;
    }
    
    public ConstantBooleanCriterion(final boolean value) {
        super();
        this.m_value = value;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (this.m_value) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public Enum getEnum() {
        return CriterionIds.BOOL;
    }
    
    static {
        TRUE = new ConstantBooleanCriterion(true);
        FALSE = new ConstantBooleanCriterion(false);
    }
}
