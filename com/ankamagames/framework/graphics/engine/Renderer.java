package com.ankamagames.framework.graphics.engine;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;

public abstract class Renderer<Device>
{
    protected Device m_device;
    protected Matrix44 m_worldMatrix;
    protected Matrix44 m_cameraMatrix;
    protected final Matrix44 m_worldViewProjMatrix;
    protected Texture m_renderTarget;
    protected int m_maxMultiTextures;
    private final BatchStates m_batchStates;
    private final boolean USE_BATCH = false;
    protected int m_viewportWidth;
    protected int m_viewportHeight;
    
    protected Renderer() {
        super();
        this.m_maxMultiTextures = 1;
        this.m_batchStates = new BatchStates();
        this.m_worldViewProjMatrix = Matrix44.Factory.newInstance();
    }
    
    public void render() {
    }
    
    public abstract RendererType getType();
    
    public abstract Texture createTexture(final long p0, final String p1, final boolean p2);
    
    public abstract Texture createTexture(final long p0, final Image p1, final boolean p2);
    
    public abstract Texture createRenderTarget(final long p0, final int p1, final int p2, final boolean p3);
    
    public abstract void setWorldMatrix(final Matrix44 p0);
    
    public abstract void setCameraMatrix(final Matrix44 p0);
    
    public abstract void updateMatrix();
    
    public final Matrix44 getWorldMatrix() {
        return this.m_worldMatrix;
    }
    
    public final Matrix44 getCameraMatrix() {
        return this.m_cameraMatrix;
    }
    
    public abstract void setRenderTarget(final Texture p0);
    
    public void draw(final Renderable renderable) {
        renderable.render(this);
    }
    
    public final void begin() {
    }
    
    public final void flush() {
    }
    
    public final void end() {
    }
    
    public void createDevice(final Device device) {
        this.m_device = device;
    }
    
    public final Device getDevice() {
        return this.m_device;
    }
    
    public abstract void drawRect(final float p0, final float p1, final float p2, final float p3, final int p4);
    
    public abstract boolean supportRenderTarget();
    
    public final Matrix44 getWorldViewProjMatrix() {
        return this.m_worldViewProjMatrix;
    }
    
    public final void setViewportSize(final int width, final int height) {
        this.m_viewportWidth = width;
        this.m_viewportHeight = height;
    }
    
    public final int getViewportWidth() {
        return this.m_viewportWidth;
    }
    
    public final int getViewportHeight() {
        return this.m_viewportHeight;
    }
}
