package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.xulor2.core.*;

public class NullCondition extends Condition
{
    public static final String TAG = "isNull";
    
    @Override
    public String getTag() {
        return "isNull";
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        return object == null;
    }
    
    public Condition cloneCondition() {
        final NullCondition clone = new NullCondition();
        this.copyElement(clone);
        return clone;
    }
}
