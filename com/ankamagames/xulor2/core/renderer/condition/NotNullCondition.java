package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.xulor2.core.*;

public class NotNullCondition extends Condition
{
    public static final String TAG = "isNotNull";
    
    @Override
    public String getTag() {
        return "isNotNull";
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        return object != null;
    }
    
    public Condition cloneCondition() {
        final NotNullCondition clone = new NotNullCondition();
        this.copyElement(clone);
        return clone;
    }
}
