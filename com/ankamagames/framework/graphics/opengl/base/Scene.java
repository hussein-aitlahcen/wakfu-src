package com.ankamagames.framework.graphics.opengl.base;

import com.ankamagames.framework.graphics.opengl.base.render.*;
import javax.media.opengl.*;

public class Scene implements GLRenderable
{
    private final ViewPort m_viewport;
    protected float m_frustumWidth;
    protected float m_frustumHeight;
    protected boolean m_visible;
    private boolean m_initialized;
    
    public Scene() {
        super();
        this.m_viewport = new ViewPort(0, 0, 0, 0);
        this.setVisible(false);
    }
    
    public final ViewPort getViewPort() {
        return this.m_viewport;
    }
    
    public float getFrustumWidth() {
        return this.m_frustumWidth;
    }
    
    public float getFrustumHeight() {
        return this.m_frustumHeight;
    }
    
    @Override
    public void setFrustumSize(final int frustumWidth, final int frustumHeight) {
        this.m_frustumWidth = frustumWidth;
        this.m_frustumHeight = frustumHeight;
        this.m_viewport.set(0, 0, frustumWidth, frustumHeight);
    }
    
    @Override
    public void init(final GLAutoDrawable glAutoDrawable) {
        this.setFrustumSize(glAutoDrawable.getWidth(), glAutoDrawable.getHeight());
    }
    
    @Override
    public void process(final int deltaTime) {
    }
    
    @Override
    public void display(final GL gl) {
    }
    
    public void uninitialize() {
        this.setInitialized(false);
        this.m_visible = true;
    }
    
    public boolean isInitialized() {
        return this.m_initialized;
    }
    
    public void setInitialized(final boolean initialized) {
        this.m_initialized = initialized;
    }
    
    public void setVisible(final boolean visible) {
        this.m_visible = visible;
    }
    
    public boolean isVisible() {
        return this.m_visible;
    }
    
    public void onCheckOut() {
        if (!this.isInitialized()) {
            this.setInitialized(true);
            this.m_visible = true;
        }
    }
    
    public void onCheckIn() {
        if (this.isInitialized()) {
            this.uninitialize();
        }
    }
}
