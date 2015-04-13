package com.ankamagames.framework.ai.criteria.antlrcriteria;

public class IfCriterion extends SimpleCriterion
{
    private SimpleCriterion m_condition;
    private SimpleCriterion m_then;
    private SimpleCriterion m_else;
    
    public static ParserObject generate(final ParserObject condition, final ParserObject then, final ParserObject lse) {
        if (condition == null || condition.getType() != ParserType.BOOLEAN) {
            throw new ParseException("La condition d'un if doit \u00eatre un bool\u00e9en");
        }
        if (then == null || lse == null || then.getType() != lse.getType()) {
            throw new ParseException("Les deux branches d'un if doivent exister et avoir le m\u00eame type ");
        }
        if (then.getType() == ParserType.BOOLEAN) {
            if (!((SimpleCriterion)condition).isConstant()) {
                return new IfCriterion((SimpleCriterion)condition, (SimpleCriterion)then, (SimpleCriterion)lse);
            }
            if (((SimpleCriterion)condition).isValid(null, null, null, null)) {
                if (((SimpleCriterion)then).isConstant()) {
                    return new ConstantBooleanCriterion(((SimpleCriterion)then).isValid(null, null, null, null));
                }
                return then;
            }
            else {
                if (((SimpleCriterion)lse).isConstant()) {
                    return new ConstantBooleanCriterion(((SimpleCriterion)lse).isValid(null, null, null, null));
                }
                return lse;
            }
        }
        else {
            if (then.getType() != ParserType.NUMBER && (then.getType() != ParserType.POSITION || then.getType() != lse.getType())) {
                throw new ParseException("Les deux branches d'un if doivent \u00eatre soit bool\u00e9ennes soit num\u00e9riques");
            }
            if (!((SimpleCriterion)condition).isConstant()) {
                return new IfValue((SimpleCriterion)condition, (NumericalValue)then, (NumericalValue)lse);
            }
            if (((SimpleCriterion)condition).isValid(null, null, null, null)) {
                return then;
            }
            return lse;
        }
    }
    
    public IfCriterion(final SimpleCriterion condition, final SimpleCriterion then, final SimpleCriterion lse) {
        super();
        this.m_condition = condition;
        this.m_then = then;
        this.m_else = lse;
    }
    
    @Override
    public int getValidity(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        if (this.m_condition.isValid(criterionUser, criterionTarget, criterionContent, criterionContext)) {
            return this.m_then.getValidity(criterionUser, criterionTarget, criterionContent, criterionContext);
        }
        return this.m_else.getValidity(criterionUser, criterionTarget, criterionContent, criterionContext);
    }
    
    @Override
    public boolean isConstant() {
        return false;
    }
    
    @Override
    public Enum getEnum() {
        return CriterionIds.IF;
    }
}
