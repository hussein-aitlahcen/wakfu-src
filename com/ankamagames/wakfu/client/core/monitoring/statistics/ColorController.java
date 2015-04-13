package com.ankamagames.wakfu.client.core.monitoring.statistics;

public abstract class ColorController
{
    private SimpleElementView m_parent;
    protected final String RED = "ff0000";
    protected final String GREEN = "00ff00";
    protected final String ORANGE = "ff9922";
    
    public abstract String getColorFromValue(final Object p0);
    
    protected SimpleElementView getParent() {
        return this.m_parent;
    }
    
    public void setParent(final SimpleElementView parent) {
        this.m_parent = parent;
    }
}
