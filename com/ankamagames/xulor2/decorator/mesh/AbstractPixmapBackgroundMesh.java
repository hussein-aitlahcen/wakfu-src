package com.ankamagames.xulor2.decorator.mesh;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.entity.batch.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import java.util.*;

public abstract class AbstractPixmapBackgroundMesh extends AbstractBackgroundMesh
{
    protected Color m_modulationColor;
    protected Pixmap[] m_pixmaps;
    protected Entity3D m_entity;
    private Entity3DBatcher m_batcher;
    
    public AbstractPixmapBackgroundMesh() {
        super();
        this.m_modulationColor = null;
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
        if (this.m_pixmaps != null) {
            Arrays.fill(this.m_pixmaps, null);
        }
        this.m_entity.removeReference();
        this.m_entity = null;
        if (this.m_batcher != null) {
            this.m_batcher.release();
            this.m_batcher = null;
        }
        this.m_modulationColor = null;
    }
    
    @Override
    public void onCheckOut() {
        assert this.m_entity == null : "Object already checked out";
        this.m_entity = Entity3D.Factory.newPooledInstance();
    }
    
    protected final void addGeometry(final int left, final int top, final int width, final int height, final Pixmap pixmap, final float texCoordTop, final float texCoordLeft, final float texCoordBottom, final float texCoordRight) {
        if (width == 0 || height == 0 || pixmap == null || pixmap.getTexture() == null) {
            return;
        }
        this.m_batcher.fillBuffer(left, top, width, height, pixmap, texCoordTop, texCoordLeft, texCoordBottom, texCoordRight, this.m_modulationColor);
    }
    
    protected final void addGeometry(final int left, final int top, final int width, final int height, final Pixmap pixmap) {
        if (width == 0 || height == 0 || pixmap == null || pixmap.getTexture() == null) {
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
