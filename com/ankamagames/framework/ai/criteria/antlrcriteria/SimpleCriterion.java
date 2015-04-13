package com.ankamagames.framework.ai.criteria.antlrcriteria;

import org.apache.log4j.*;

public abstract class SimpleCriterion extends ParserObject
{
    public static final int VALID_CRITERION_VALUE = 0;
    public static final int INVALID_CRITERION_VALUE = -1;
    private boolean m_negated;
    protected static final Logger m_logger;
    
    public SimpleCriterion() {
        super();
        this.m_negated = false;
    }
    
    public SimpleCriterion setNegate() {
        if (!this.isConstant()) {
            this.m_negated = !this.m_negated;
            return this;
        }
        return new ConstantBooleanCriterion(!this.isValid(null, null, null, null));
    }
    
    public abstract boolean isConstant();
    
    public abstract int getValidity(final Object p0, final Object p1, final Object p2, final Object p3);
    
    public boolean isValid(final Object criterionUser, final Object criterionTarget, final Object criterionContent, final Object criterionContext) {
        boolean valid;
        try {
            valid = (this.getValidity(criterionUser, criterionTarget, criterionContent, criterionContext) == 0);
            if (this.m_negated) {
                return !valid;
            }
        }
        catch (CriteriaExecutionException e) {
            valid = false;
            SimpleCriterion.m_logger.error((Object)("Exception sur crit\u00e8re (expected) !! User : " + criterionUser + " Target : " + criterionTarget + " Context : " + criterionContext), (Throwable)e);
        }
        catch (Exception e2) {
            valid = false;
            SimpleCriterion.m_logger.error((Object)"Exception inattendue", (Throwable)e2);
        }
        return valid;
    }
    
    @Override
    public ParserType getType() {
        return ParserType.BOOLEAN;
    }
    
    public boolean isNegated() {
        return this.m_negated;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SimpleCriterion.class);
    }
}
