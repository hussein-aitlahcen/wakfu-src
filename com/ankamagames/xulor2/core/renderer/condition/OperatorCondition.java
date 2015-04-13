package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.xulor2.core.*;

public abstract class OperatorCondition extends Condition
{
    @Override
    public void add(final EventDispatcher childElement) {
        if (childElement instanceof Condition) {
            this.addCondition((Condition)childElement);
        }
        super.add(childElement);
    }
    
    public abstract void addCondition(final Condition p0);
}
