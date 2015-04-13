package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.component.mesh.mapHelper.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;
import javax.media.opengl.*;
import com.ankamagames.xulor2.graphics.*;

public final class MapNavigatorMesh2 extends MapOverlayMesh
{
    private float m_centerX;
    private float m_centerY;
    private float m_targetX;
    private float m_targetY;
    private int m_parentWidth;
    private int m_parentHeight;
    private WidgetShape m_shape;
    
    public MapNavigatorMesh2() {
        super();
        this.m_shape = WidgetShape.RECTANGLE;
    }
    
    @Override
    public void setMapDisplayer(final MapDisplayer mapDisplayer) {
        super.setMapDisplayer(mapDisplayer);
        final BatchTransformer transformer = this.m_mapDisplayer.getEntity().getTransformer();
        transformer.addTransformer(this.m_worldTransformation);
        transformer.addTransformer(this.m_cameraTransformation);
    }
    
    public void setXY(final float x, final float y) {
        this.m_targetX = x;
        this.m_targetY = y;
        this.recomputeCameraPosition(null);
    }
    
    public void setSize(final int parentWidth, final int parentHeight) {
        this.m_parentWidth = parentWidth;
        this.m_parentHeight = parentHeight;
        this.recomputeCameraPosition(null);
    }
    
    public void setCenter(final float centerX, final float centerY) {
        this.m_centerX = centerX;
        this.m_centerY = centerY;
        this.recomputeCameraPosition(null);
    }
    
    @Override
    protected void recomputeCameraPosition(final Dimension size) {
        final float xRatio = (this.m_targetX + this.m_centerX) * this.m_zoomFactor;
        final float yRatio = (this.m_targetY + this.m_centerY) * this.m_zoomFactor;
        this.updateCameraTranslation(this.m_parentWidth / 2 - xRatio, this.m_parentHeight / 2 - yRatio);
    }
    
    @Override
    protected void updateEntity() {
        if (this.m_entity.getChildList().isEmpty()) {
            this.m_entity.addChild(this.m_mapDisplayer.getEntity());
        }
        else {
            this.m_entity.setChild(0, this.m_mapDisplayer.getEntity());
        }
    }
    
    private static class CircleShapePreRenderState extends RenderStates
    {
        public int m_width;
        public int m_height;
        
        @Override
        public void apply(final Renderer renderer) {
            final GLRenderer glRenderer = (GLRenderer)renderer;
            final RenderStateManager stateManager = RenderStateManager.getInstance();
            final GL gl = glRenderer.getDevice();
            gl.glClear(1024);
            stateManager.enableTextures(false);
            stateManager.applyStates(renderer);
            final int radius = Math.min(this.m_width, this.m_height) / 2;
            Graphics.getInstance().drawCircle(this.m_width / 2, this.m_height / 2, radius);
            stateManager.enableTextures(true);
        }
    }
    
    private static class CircleShapePostRenderState extends RenderStates
    {
        @Override
        public void apply(final Renderer renderer) {
            final RenderStateManager stateManager = RenderStateManager.getInstance();
        }
    }
}
