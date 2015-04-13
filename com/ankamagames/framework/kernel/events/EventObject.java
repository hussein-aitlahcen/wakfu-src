package com.ankamagames.framework.kernel.events;

public class EventObject
{
    protected Object m_source;
    
    public EventObject(final Object source) {
        super();
        this.m_source = source;
    }
    
    public Object getSource() {
        return this.m_source;
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + "[source=" + this.m_source + "]";
    }
}
