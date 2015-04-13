package com.ankamagames.xulor2.component.mesh;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.mesh.mapHelper.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import java.awt.*;

public abstract class MapOverlayMesh
{
    private static final Logger m_logger;
    protected MapDisplayer m_mapDisplayer;
    protected EntityGroup m_entity;
    protected TransformerSRT m_worldTransformation;
    protected TransformerSRT m_cameraTransformation;
    private Color m_modulationColor;
    protected Pixmap m_pixmap;
    protected float m_zoomFactor;
    protected int m_height;
    protected int m_width;
    private float m_cameraPosX;
    private float m_cameraPosY;
    
    public MapOverlayMesh() {
        super();
        this.m_zoomFactor = 1.0f;
        this.m_cameraPosX = Float.NaN;
        this.m_cameraPosY = Float.NaN;
    }
    
    public void setMapDisplayer(final MapDisplayer mapDisplayer) {
        if (this.m_mapDisplayer != null) {
            this.m_mapDisplayer.clear();
        }
        (this.m_mapDisplayer = mapDisplayer).init();
    }
    
    public final int getHeight() {
        return this.m_height;
    }
    
    public final void setHeight(final int height) {
        this.m_height = height;
    }
    
    public final int getWidth() {
        return this.m_width;
    }
    
    public final void setWidth(final int width) {
        this.m_width = width;
    }
    
    public final Entity getEntity() {
        return this.m_entity;
    }
    
    public final Pixmap getPixmap() {
        return this.m_pixmap;
    }
    
    public final Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    public void setPixmap(final Pixmap pixmap) {
        this.m_pixmap = pixmap;
    }
    
    public final void setModulationColor(final Color c) {
        if (this.m_modulationColor == c) {
            return;
        }
        this.m_modulationColor = c;
    }
    
    public final void setZoomFactor(final float zoomFactor) {
        this.m_zoomFactor = zoomFactor;
        this.m_worldTransformation.setScale(this.m_zoomFactor, this.m_zoomFactor, this.m_zoomFactor);
        this.updateEntitiesWithTransformers();
    }
    
    protected final void updateCameraTranslation(final float x, final float y) {
        if (this.m_cameraPosX == x && this.m_cameraPosY == y) {
            return;
        }
        this.m_cameraPosX = x;
        this.m_cameraPosY = y;
        this.m_cameraTransformation.setTranslation(x, y, -10000.0f);
        this.updateEntitiesWithTransformers();
    }
    
    protected void updateEntitiesWithTransformers() {
        if (this.m_mapDisplayer == null) {
            return;
        }
        this.m_mapDisplayer.getEntity().getTransformer().setToUpdate();
    }
    
    protected abstract void updateEntity();
    
    public void updateVertex(final Dimension size, final Insets margin, final Insets border, final Insets padding) {
        this.recomputeCameraPosition(size);
        if (this.m_mapDisplayer != null) {
            this.m_mapDisplayer.updateGeometry(this);
            this.updateEntity();
            this.updateEntitiesWithTransformers();
        }
    }
    
    protected abstract void recomputeCameraPosition(final Dimension p0);
    
    public final void setMapSize(final int width, final int height) {
        this.m_width = width;
        this.m_height = height;
    }
    
    public void onCheckIn() {
        this.m_pixmap = null;
        this.m_entity.removeReference();
        this.m_entity = null;
        this.m_modulationColor = null;
        if (this.m_mapDisplayer != null) {
            this.m_mapDisplayer.clear();
            this.m_mapDisplayer = null;
        }
    }
    
    public void onCheckOut() {
        assert this.m_entity == null;
        assert this.m_mapDisplayer == null;
        this.m_entity = EntityGroup.Factory.newPooledInstance();
        this.m_entity.m_owner = this;
        this.m_worldTransformation = new TransformerSRT();
        this.m_cameraTransformation = new TransformerSRT();
        this.m_cameraPosX = Float.NaN;
        this.m_cameraPosY = Float.NaN;
    }
    
    public final Color getColor() {
        return (this.m_modulationColor == null) ? Color.WHITE : this.m_modulationColor;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapOverlayMesh.class);
    }
}
