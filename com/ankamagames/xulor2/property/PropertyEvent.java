package com.ankamagames.xulor2.property;

public class PropertyEvent
{
    private PropertyEventType m_type;
    private Property m_property;
    
    public PropertyEvent(final PropertyEventType type, final Property p) {
        super();
        this.m_type = type;
        this.m_property = p;
    }
    
    public PropertyEventType getType() {
        return this.m_type;
    }
    
    public Property getProperty() {
        return this.m_property;
    }
    
    public enum PropertyEventType
    {
        PROPERTY_ADDED, 
        PROPERTY_REMOVED, 
        PROPERTY_INIT, 
        PROPERTY_VALUE_CHANGED;
    }
}
