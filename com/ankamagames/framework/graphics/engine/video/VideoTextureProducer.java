package com.ankamagames.framework.graphics.engine.video;

import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class VideoTextureProducer
{
    protected int NO_IMAGE_UID;
    private Texture m_texture;
    private Layer m_renderingLayer;
    private long m_lastVideoImageUid;
    private float m_bottom;
    private float m_top;
    private float m_left;
    private float m_right;
    
    public VideoTextureProducer() {
        super();
        this.NO_IMAGE_UID = -1;
        this.m_bottom = 0.0f;
        this.m_top = 1.0f;
        this.m_left = 0.0f;
        this.m_right = 1.0f;
        this.m_lastVideoImageUid = this.NO_IMAGE_UID;
    }
    
    protected void initialize() {
        this.m_texture = new GLTexture(TextureConstants.getNextRenderTargetId(), 32, 32, false);
        (this.m_renderingLayer = this.m_texture.getLayer(0)).setPixelFormat(this.getPixelFormat());
        this.updateTexCoords();
        this.m_lastVideoImageUid = -1L;
    }
    
    public abstract byte getPixelFormat();
    
    public void updateTexture(final Renderer renderer) {
        final long currentImageUid = this.getImageUid();
        if (currentImageUid == this.m_lastVideoImageUid || currentImageUid == this.NO_IMAGE_UID) {
            return;
        }
        final ByteBuffer imageData = this.getImageData();
        if (imageData == null) {
            return;
        }
        this.m_renderingLayer.updateData(imageData, this.getImageWidth(), this.getImageHeight());
        this.m_texture.updateTextureData(renderer);
        this.updateTexCoords();
        this.m_lastVideoImageUid = currentImageUid;
    }
    
    protected abstract int getImageWidth();
    
    protected abstract int getImageHeight();
    
    protected abstract long getImageUid();
    
    protected abstract ByteBuffer getImageData();
    
    private void updateTexCoords() {
        final Point2i textureSize = this.m_texture.getSize(0);
        final float videoWidth = this.getImageWidth();
        final float videoHeight = this.getImageHeight();
        this.m_bottom = 0.0f;
        this.m_top = videoHeight / textureSize.getY();
        this.m_left = 0.0f;
        this.m_right = videoWidth / textureSize.getX();
    }
    
    public boolean hasTexture() {
        return this.m_texture != null;
    }
    
    public Texture getTexture() {
        return this.m_texture;
    }
    
    public float getBottom() {
        return this.m_bottom;
    }
    
    public float getTop() {
        return this.m_top;
    }
    
    public float getLeft() {
        return this.m_left;
    }
    
    public float getRight() {
        return this.m_right;
    }
    
    public int getSourceWidth() {
        return this.getImageWidth();
    }
    
    public int getSourceHeight() {
        return this.getImageHeight();
    }
}
