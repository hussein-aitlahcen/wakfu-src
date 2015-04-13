package com.ankamagames.wakfu.client.core.monitoring.statistics;

public final class ConstantDateElementView extends DateElementView
{
    public ConstantDateElementView(final String name, final Long time, final ColorController colorController) {
        super(name, null, colorController);
        this.m_value = time;
    }
    
    public ConstantDateElementView(final String name, final Long time) {
        super(name, null);
        this.m_value = time;
    }
}
