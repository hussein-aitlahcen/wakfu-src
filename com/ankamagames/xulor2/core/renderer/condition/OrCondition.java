package com.ankamagames.xulor2.core.renderer.condition;

import java.util.*;
import com.ankamagames.xulor2.core.*;

public class OrCondition extends MultipleConditionOperator
{
    public static final String TAG = "Or";
    
    @Override
    public String getTag() {
        return "Or";
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        for (final Condition condition : this.m_conditions) {
            if (condition.isValid(object)) {
                return true;
            }
        }
        return false;
    }
    
    public Condition cloneCondition() {
        final OrCondition clone = new OrCondition();
        this.copyElement(clone);
        return clone;
    }
}
