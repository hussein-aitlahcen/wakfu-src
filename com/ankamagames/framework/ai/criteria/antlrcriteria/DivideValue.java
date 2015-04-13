package com.ankamagames.framework.ai.criteria.antlrcriteria;

public class DivideValue extends NumericalOperator
{
    private NumericalValue m_left;
    private NumericalValue m_right;
    
    private DivideValue(final NumericalValue left, final NumericalValue right) {
        super();
        this.m_left = left;
        this.m_right = right;
    }
    
    public static NumericalValue generate(final ParserObject pleft, final ParserObject pright) {
        NumericalOperator.checkType(pleft, pright);
        final NumericalValue left = (NumericalValue)pleft;
        final NumericalValue right = (NumericalValue)pright;
        if (left.isConstant() && right.isConstant()) {
            return new ConstantDoubleValue((left.isInteger() ? left.getLongValue(null, null, null, null) : left.getDoubleValue(null, null, null, null)) / (right.isInteger() ? right.getLongValue(null, null, null, null) : right.getDoubleValue(null, null, null, null)));
        }
        return new DivideValue(left, right);
    }
    
    @Override
    public boolean isInteger() {
        return false;
    }
    
    @Override
    public double getDoubleValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        final double value = (this.m_left.isInteger() ? this.m_left.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext) : this.m_left.getDoubleValue(criterionUser, criterionTarget, criterionContent, criterionContext)) / (this.m_right.isInteger() ? this.m_right.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext) : this.m_right.getDoubleValue(criterionUser, criterionTarget, criterionContent, criterionContext));
        if (this.isOpposite()) {
            return -1.0 * value;
        }
        return value;
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        throw new UnsupportedOperationException("Pas de division enti\u00e8re pour le moment");
    }
    
    @Override
    public boolean isConstant() {
        return false;
    }
    
    @Override
    public Enum getEnum() {
        return CriterionIds.DIVIDE;
    }
}
