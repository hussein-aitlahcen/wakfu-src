package com.ankamagames.xulor2.core.renderer.condition;

import java.util.*;
import com.ankamagames.xulor2.core.*;

public class AndCondition extends MultipleConditionOperator
{
    public static final String TAG = "And";
    
    @Override
    public String getTag() {
        return "And";
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        for (final Condition condition : this.m_conditions) {
            if (!condition.isValid(object)) {
                return false;
            }
        }
        return true;
    }
    
    public Condition cloneCondition() {
        final AndCondition clone = new AndCondition();
        this.copyElement(clone);
        return clone;
    }
    
    public Object getEncapsulatedObject() {
        return null;
    }
}
