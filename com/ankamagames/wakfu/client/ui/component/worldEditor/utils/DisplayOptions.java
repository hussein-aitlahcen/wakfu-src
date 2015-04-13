package com.ankamagames.wakfu.client.ui.component.worldEditor.utils;

public final class DisplayOptions
{
    public static final float ALPHA_BUILDING = 0.4f;
    public final boolean m_drawGrid;
    public final boolean m_maskBuilding;
    
    public DisplayOptions(final boolean drawGrid, final boolean maskBuilding) {
        super();
        this.m_drawGrid = drawGrid;
        this.m_maskBuilding = maskBuilding;
    }
}
