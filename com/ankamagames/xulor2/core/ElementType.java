package com.ankamagames.xulor2.core;

public enum ElementType
{
    EVENT_DISPATCHER((Class<? extends BasicElement>)EventDispatcher.class), 
    DATA((Class<? extends BasicElement>)DataElement.class);
    
    private Class<? extends BasicElement> m_type;
    
    private ElementType(final Class<? extends BasicElement> type) {
        this.m_type = type;
    }
    
    public Class<? extends BasicElement> getType() {
        return this.m_type;
    }
}
