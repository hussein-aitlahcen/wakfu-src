package com.ankamagames.framework.reflect;

public interface PropertiesProvider
{
    void setPropertyValue(String p0, Object p1);
    
    void firePropertyValueChanged(FieldProvider p0, String... p1);
}
