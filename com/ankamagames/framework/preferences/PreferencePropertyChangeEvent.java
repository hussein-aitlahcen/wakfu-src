package com.ankamagames.framework.preferences;

import com.ankamagames.framework.kernel.events.*;

public class PreferencePropertyChangeEvent extends EventObject
{
    private final String m_propertyName;
    private final Object m_oldValue;
    private final Object m_newValue;
    
    public PreferencePropertyChangeEvent(final PreferenceStore store, final String propertyName, final Object oldValue, final Object newValue) {
        super(store);
        this.m_propertyName = propertyName;
        this.m_oldValue = oldValue;
        this.m_newValue = newValue;
    }
    
    public PreferenceStore getPreferenceStore() {
        return (PreferenceStore)this.m_source;
    }
    
    public String getPropertyName() {
        return this.m_propertyName;
    }
    
    public Object getOldValue() {
        return this.m_oldValue;
    }
    
    public Object getNewValue() {
        return this.m_newValue;
    }
}
