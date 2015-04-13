package com.ankamagames.framework.ai.criteria.antlrcriteria;

import org.jetbrains.annotations.*;

public abstract class NumericalValue extends ParserObject
{
    public abstract boolean isInteger();
    
    public abstract void setOpposite();
    
    public double getDoubleValue(@Nullable final Object criterionUser, @Nullable final Object criterionTarget, @Nullable final Object criterionContent, @Nullable final Object criterionContext) {
        return this.getLongValue(criterionUser, criterionTarget, criterionContent, criterionContext);
    }
    
    public abstract long getLongValue(@Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3);
    
    public abstract boolean isConstant();
    
    @Override
    public ParserType getType() {
        return ParserType.NUMBER;
    }
}
