package com.ankamagames.xulor2.core.renderer.condition;

import org.apache.log4j.*;

public class FalseCondition extends Condition
{
    private static Logger m_logger;
    public static final String TAG = "isFalse";
    
    @Override
    public String getTag() {
        return "isFalse";
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        return object instanceof Boolean && !(boolean)object;
    }
    
    static {
        FalseCondition.m_logger = Logger.getLogger((Class)TrueCondition.class);
    }
}
