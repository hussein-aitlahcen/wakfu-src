package com.ankamagames.xulor2.decorator.mesh;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.entity.batch.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public abstract class AbstractPixmapBorderMesh extends AbstractBorderMesh
{
    protected Color m_modulationColor;
    protected Pixmap[] m_pixmaps;
    protected boolean m_pixmapsInitialized;
    protected Entity3D m_entity;
    private Entity3DBatcher m_batcher;
    
    public AbstractPixmapBorderMesh() {
        super();
        this.m_modulationColor = null;
        this.m_pixmapsInitialized = false;
    }
    
    @Override
    public abstract void updateVertex(final Dimension p0, final Insets p1, final Insets p2, final Insets p3);
    
    @Override
    public final Entity getEntity() {
        return this.m_entity;
    }
    
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    public void setModulationColor(final Color modulationColor) {
        if (this.m_modulationColor == modulationColor) {
            return;
        }
        this.m_modulationColor = modulationColor;
        if (this.m_modulationColor != null) {
            this.m_entity.setColor(modulationColor.getRed(), modulationColor.getGreen(), modulationColor.getBlue(), modulationColor.getAlpha());
        }
        else {
            this.m_entity.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void onCheckIn() {
        this.m_entity.removeReference();
        this.m_entity = null;
        this.m_modulationColor = null;
        if (this.m_batcher != null) {
            this.m_batcher.release();
            this.m_batcher = null;
        }
    }
    
    @Override
    public void onCheckOut() {
        assert this.m_entity == null;
        this.m_entity = Entity3D.Factory.newPooledInstance();
    }
    
    protected final void addGeometry(final int left, final int top, final int width, final int height, final Pixmap pixmap) {
        if (width == 0 || height == 0) {
            return;
        }
        this.m_batcher.fillBuffer(left, top, width, height, pixmap, this.m_modulationColor);
    }
    
    protected final void beginAddGeometry(final int numGeom) {
        if (this.m_batcher == null) {
            this.m_batcher = new Entity3DBatcher();
        }
        this.m_batcher.beginAddGeometry(this.m_entity, numGeom);
    }
    
    protected final void endAddGeometry() {
        this.m_batcher.endAddGeometry();
    }
}
