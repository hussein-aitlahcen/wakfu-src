package com.ankamagames.framework.ai.criteria.antlrcriteria;

public class ConstantIntegerValue extends NumericalValue
{
    private long m_value;
    
    @Override
    public boolean isInteger() {
        return true;
    }
    
    @Override
    public void setOpposite() {
        this.m_value *= -1L;
    }
    
    public ConstantIntegerValue(final long value) {
        super();
        this.m_value = value;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        return this.m_value;
    }
    
    @Override
    public double getDoubleValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        return this.m_value;
    }
    
    @Override
    public boolean isConstant() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return CriterionIds.INT;
    }
}
