package com.ankamagames.xulor2.core.renderer.condition;

import java.util.*;

public abstract class MultipleConditionOperator extends OperatorCondition
{
    protected final ArrayList<Condition> m_conditions;
    
    public MultipleConditionOperator() {
        super();
        this.m_conditions = new ArrayList<Condition>();
    }
    
    @Override
    public void addCondition(final Condition condition) {
        this.m_conditions.add(condition);
    }
}
