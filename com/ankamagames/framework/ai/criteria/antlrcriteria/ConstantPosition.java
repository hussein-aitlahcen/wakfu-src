package com.ankamagames.framework.ai.criteria.antlrcriteria;

public class ConstantPosition extends PositionValue
{
    private long m_value;
    
    public ConstantPosition(final int x, final int y, final short z) {
        super();
        this.m_value = PositionValue.toLong(x, y, z);
    }
    
    @Override
    public double getDoubleValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        return Double.longBitsToDouble(this.m_value);
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        return this.m_value;
    }
    
    @Override
    public boolean isConstant() {
        return true;
    }
    
    @Override
    public Enum getEnum() {
        return CriterionIds.POS;
    }
}
