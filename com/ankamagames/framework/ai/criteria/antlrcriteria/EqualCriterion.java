package com.ankamagames.framework.ai.criteria.antlrcriteria;

public class EqualCriterion extends NumericalComparator
{
    private NumericalValue m_left;
    private NumericalValue m_right;
    
    private EqualCriterion(final NumericalValue left, final NumericalValue right) {
        super();
        this.m_left = left;
        this.m_right = right;
    }
    
    public NumericalValue getLeft() {
        return this.m_left;
    }
    
    public NumericalValue getRight() {
        return this.m_right;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if ((this.m_left.isInteger() ? this.m_left.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext) : this.m_left.getDoubleValue(criterionUser, criterionTarget, criterionContent, criterionContext)) == (this.m_right.isInteger() ? this.m_right.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext) : this.m_right.getDoubleValue(criterionUser, criterionTarget, criterionContent, criterionContext))) {
            return 0;
        }
        return -1;
    }
    
    @Override
    public boolean isConstant() {
        return false;
    }
    
    public static SimpleCriterion generate(final ParserObject pleft, final ParserObject pright) {
        NumericalComparator.checkType(pleft, pright);
        final NumericalValue left = (NumericalValue)pleft;
        final NumericalValue right = (NumericalValue)pright;
        if (left.isConstant() && right.isConstant()) {
            return new ConstantBooleanCriterion((left.isInteger() ? left.getLongValue(null, null, null, null) : left.getDoubleValue(null, null, null, null)) == (right.isInteger() ? right.getLongValue(null, null, null, null) : right.getDoubleValue(null, null, null, null)));
        }
        return new EqualCriterion(left, right);
    }
    
    @Override
    public Enum getEnum() {
        return CriterionIds.EQUAL;
    }
}
