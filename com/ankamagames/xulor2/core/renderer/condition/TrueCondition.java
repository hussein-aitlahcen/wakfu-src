package com.ankamagames.xulor2.core.renderer.condition;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;

public class TrueCondition extends Condition
{
    private static Logger m_logger;
    public static final String TAG = "isTrue";
    
    @Override
    public String getTag() {
        return "isTrue";
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        return object instanceof Boolean && (boolean)object;
    }
    
    public Condition cloneCondition() {
        final TrueCondition clone = new TrueCondition();
        this.copyElement(clone);
        return clone;
    }
    
    static {
        TrueCondition.m_logger = Logger.getLogger((Class)TrueCondition.class);
    }
}
