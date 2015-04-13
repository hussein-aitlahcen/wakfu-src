package com.ankamagames.xulor2.core.renderer.condition;

import java.util.*;

public class CollectionCondition extends UnaryConditionOperator
{
    public static final String TAG = "CollectionCondition";
    public static final String SIZE_KEY = "size";
    
    @Override
    public String getTag() {
        return "CollectionCondition";
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        if (this.m_key == null || !this.m_key.equalsIgnoreCase("size")) {
            return false;
        }
        if (object instanceof Collection) {
            return this.m_condition.isValid(((Collection)object).size());
        }
        return object instanceof Object[] && this.m_condition.isValid(((Object[])object).length);
    }
}
