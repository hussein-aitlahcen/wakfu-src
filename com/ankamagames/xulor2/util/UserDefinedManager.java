package com.ankamagames.xulor2.util;

import com.ankamagames.xulor2.core.*;

public abstract class UserDefinedManager<T extends EventDispatcher>
{
    protected T m_element;
    
    public UserDefinedManager(final T element) {
        super();
        this.m_element = element;
        this.addToGlobalManager();
    }
    
    public void loadPreferences() {
        final ElementMap map = this.m_element.getElementMap();
        if (map == null) {
            return;
        }
        final String mapId = map.getId();
        final String elementId = this.m_element.getId();
        if (mapId == null) {
            return;
        }
        this.doLoadPreferences(mapId, elementId);
    }
    
    public void storePreferences() {
        final ElementMap map = this.m_element.getElementMap();
        if (map == null) {
            return;
        }
        final String mapId = map.getId();
        final String elementId = this.m_element.getId();
        if (mapId == null) {
            return;
        }
        this.doStorePreferences(mapId, elementId);
    }
    
    protected abstract void doLoadPreferences(final String p0, final String p1);
    
    protected abstract void doStorePreferences(final String p0, final String p1);
    
    public abstract void removeFromGlobalManager();
    
    public abstract void addToGlobalManager();
    
    public abstract boolean hasRecord();
}
