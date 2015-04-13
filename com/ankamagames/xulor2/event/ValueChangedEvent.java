package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;

public class ValueChangedEvent extends Event
{
    private Object m_value;
    private Object m_oldValue;
    
    public ValueChangedEvent(final Widget target) {
        super();
        this.m_target = target;
    }
    
    public void setValue(final Object value) {
        this.m_value = value;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public Object getOldValue() {
        return this.m_oldValue;
    }
    
    public void setOldValue(final Object oldValue) {
        this.m_oldValue = oldValue;
    }
    
    @Override
    public Events getType() {
        return Events.VALUE_CHANGED;
    }
}
