package com.ankamagames.framework.ai.criteria.antlrcriteria;

public class AddValue extends NumericalOperator
{
    private NumericalValue m_left;
    private NumericalValue m_right;
    
    private AddValue(final NumericalValue left, final NumericalValue right) {
        super();
        this.m_left = left;
        this.m_right = right;
    }
    
    public static NumericalValue generate(final ParserObject pleft, final ParserObject pright) {
        NumericalOperator.checkType(pleft, pright);
        final NumericalValue left = (NumericalValue)pleft;
        final NumericalValue right = (NumericalValue)pright;
        if (!left.isConstant() || !right.isConstant()) {
            return new AddValue(left, right);
        }
        if (left.isInteger() && right.isInteger()) {
            return new ConstantIntegerValue(left.getLongValue(null, null, null, null) + right.getLongValue(null, null, null, null));
        }
        return new ConstantDoubleValue(left.getDoubleValue(null, null, null, null) + right.getDoubleValue(null, null, null, null));
    }
    
    @Override
    public boolean isInteger() {
        return this.m_left.isInteger() && this.m_right.isInteger();
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final long value = this.m_left.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext) + this.m_right.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (this.isOpposite()) {
            return -1L * value;
        }
        return value;
    }
    
    @Override
    public double getDoubleValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final double value = this.m_left.getDoubleValue(criterionUser, criterionTarget, criterionContent, criterionContext) + this.m_right.getDoubleValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        if (this.isOpposite()) {
            return -1.0 * value;
        }
        return value;
    }
    
    @Override
    public boolean isConstant() {
        return false;
    }
    
    @Override
    public Enum getEnum() {
        return CriterionIds.ADD;
    }
    
    public NumericalValue getLeft() {
        return this.m_left;
    }
    
    public NumericalValue getRight() {
        return this.m_right;
    }
}
