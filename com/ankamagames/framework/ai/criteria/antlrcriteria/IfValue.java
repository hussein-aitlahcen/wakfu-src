package com.ankamagames.framework.ai.criteria.antlrcriteria;

public class IfValue extends NumericalValue
{
    private SimpleCriterion m_condition;
    private NumericalValue m_then;
    private NumericalValue m_else;
    private boolean m_opposite;
    
    public IfValue(final SimpleCriterion condition, final NumericalValue then, final NumericalValue lse) {
        super();
        this.m_opposite = false;
        this.m_condition = condition;
        this.m_then = then;
        this.m_else = lse;
    }
    
    @Override
    public double getDoubleValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (this.m_condition.isValid(criterionUser, criterionTarget, criterionContent, criterionContext)) {
            return (this.m_opposite ? -1 : 1) * (this.m_then.isInteger() ? this.m_then.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext) : this.m_then.getDoubleValue(criterionUser, criterionTarget, criterionContent, criterionContext));
        }
        return (this.m_opposite ? -1 : 1) * (this.m_else.isInteger() ? this.m_else.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext) : this.m_else.getDoubleValue(criterionUser, criterionTarget, criterionContent, criterionContext));
    }
    
    @Override
    public long getLongValue(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (!this.isInteger()) {
            throw new UnsupportedOperationException("Cant't get integer value of a double");
        }
        if (this.m_condition.isValid(criterionUser, criterionTarget, criterionContent, criterionContext)) {
            return (this.m_opposite ? -1 : 1) * this.m_then.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
        }
        return (this.m_opposite ? -1 : 1) * this.m_else.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
    }
    
    @Override
    public boolean isInteger() {
        return this.m_then.isInteger() && this.m_else.isInteger();
    }
    
    @Override
    public void setOpposite() {
        this.m_opposite = !this.m_opposite;
    }
    
    @Override
    public boolean isConstant() {
        return false;
    }
    
    @Override
    public Enum getEnum() {
        return CriterionIds.IF;
    }
    
    @Override
    public ParserType getType() {
        return this.m_then.getType();
    }
}
