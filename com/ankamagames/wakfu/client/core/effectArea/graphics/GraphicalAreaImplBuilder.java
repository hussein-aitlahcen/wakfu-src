package com.ankamagames.wakfu.client.core.effectArea.graphics;

import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;

public final class GraphicalAreaImplBuilder
{
    private final AbstractEffectArea m_linkedArea;
    private String m_gfx;
    private String m_cellGfx;
    private String m_aps;
    private int m_apsLevel;
    private String m_cellAps;
    private short m_visualHeight;
    
    public GraphicalAreaImplBuilder(final AbstractEffectArea linkedArea) {
        super();
        this.m_apsLevel = ParticleSystemFactory.SYSTEM_WITHOUT_LEVEL;
        this.m_linkedArea = linkedArea;
    }
    
    public GraphicalAreaImplBuilder withGfx(final String gfx) {
        this.m_gfx = gfx;
        return this;
    }
    
    public GraphicalAreaImplBuilder withCellGfx(final String cellGfx) {
        this.m_cellGfx = cellGfx;
        return this;
    }
    
    public GraphicalAreaImplBuilder withAps(final String aps) {
        this.m_aps = aps;
        return this;
    }
    
    public GraphicalAreaImplBuilder withAps(final String aps, final int level) {
        this.m_aps = aps;
        this.m_apsLevel = level;
        return this;
    }
    
    public GraphicalAreaImplBuilder withCellAps(final String cellAps) {
        this.m_cellAps = cellAps;
        return this;
    }
    
    public GraphicalAreaImplBuilder withVisualHeight(final short visualHeight) {
        this.m_visualHeight = visualHeight;
        return this;
    }
    
    public GraphicalAreaImpl build() {
        final GraphicalAreaImpl graphicalAreaImpl = new GraphicalAreaImpl(this.m_linkedArea);
        graphicalAreaImpl.setAps(this.m_aps);
        graphicalAreaImpl.setCellAps(this.m_cellAps);
        graphicalAreaImpl.setAPSLevel(this.m_apsLevel);
        graphicalAreaImpl.setGfx(this.m_gfx);
        graphicalAreaImpl.setCellGfx(this.m_cellGfx);
        graphicalAreaImpl.setVisualHeight(this.m_visualHeight);
        return graphicalAreaImpl;
    }
}
