package com.ankamagames.framework.ai.criteria.antlrcriteria;

public class ConstantDoubleValue extends NumericalValue
{
    private double m_value;
    
    @Override
    public boolean isInteger() {
        return false;
    }
    
    @Override
    public void setOpposite() {
        this.m_value *= -1.0;
    }
    
    public ConstantDoubleValue(final double value) {
        super();
        this.m_value = value;
    }
    
    @Override
    public double getDoubleValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        return this.m_value;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        throw new UnsupportedOperationException("Can't get long value of a double constant");
    }
    
    @Override
    public boolean isConstant() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return CriterionIds.FLOAT;
    }
}
