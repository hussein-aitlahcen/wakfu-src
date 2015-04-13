package com.ankamagames.framework.reflect;

public class GlobalPropertiesProvider implements PropertiesProvider
{
    private static final GlobalPropertiesProvider m_instance;
    private PropertiesProvider m_listener;
    
    public static GlobalPropertiesProvider getInstance() {
        return GlobalPropertiesProvider.m_instance;
    }
    
    public void setListener(final PropertiesProvider listener) {
        this.m_listener = listener;
    }
    
    @Override
    public void firePropertyValueChanged(final FieldProvider fp, final String... fields) {
        if (this.m_listener != null) {
            this.m_listener.firePropertyValueChanged(fp, fields);
        }
    }
    
    @Override
    public void setPropertyValue(final String name, final Object value) {
        if (this.m_listener != null) {
            this.m_listener.setPropertyValue(name, value);
        }
    }
    
    static {
        m_instance = new GlobalPropertiesProvider();
    }
}
