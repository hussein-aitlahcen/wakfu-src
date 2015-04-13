package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.xulor2.core.*;

public class NotCondition extends UnaryConditionOperator
{
    public static final String TAG = "Not";
    
    @Override
    public String getTag() {
        return "Not";
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        return this.m_condition != null && !this.m_condition.isValid(object);
    }
    
    public Condition cloneCondition() {
        final NotCondition clone = new NotCondition();
        this.copyElement(clone);
        return clone;
    }
}
