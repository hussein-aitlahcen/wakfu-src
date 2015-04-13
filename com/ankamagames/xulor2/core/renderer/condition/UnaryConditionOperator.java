package com.ankamagames.xulor2.core.renderer.condition;

public abstract class UnaryConditionOperator extends OperatorCondition
{
    protected Condition m_condition;
    
    @Override
    public void addCondition(final Condition condition) {
        this.m_condition = condition;
    }
}
