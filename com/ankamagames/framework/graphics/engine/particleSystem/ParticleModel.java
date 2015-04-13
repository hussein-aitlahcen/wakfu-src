package com.ankamagames.framework.graphics.engine.particleSystem;

import com.ankamagames.framework.kernel.core.maths.*;

public abstract class ParticleModel
{
    public final float m_hotX;
    public final float m_hotY;
    public final float m_scaleX;
    public final float m_scaleY;
    public final boolean m_scaleRandomKeepRatio;
    public final float m_scaleRandomX;
    public final float m_scaleRandomY;
    public final float m_rotation;
    public final float m_rotationRandom;
    protected boolean m_textureNeedUpdate;
    public int m_bitmapId;
    public final float m_redColor;
    public final float m_greenColor;
    public final float m_blueColor;
    public final float m_alphaColor;
    public final float m_redColorRandom;
    public final float m_greenColorRandom;
    public final float m_blueColorRandom;
    public final float m_alphaColorRandom;
    private int m_textureIndex;
    public float m_textureTop;
    public float m_textureLeft;
    public float m_textureBottom;
    public float m_textureRight;
    public float m_halfWidth;
    public float m_halfHeight;
    public final float m_rotationX;
    public final float m_rotationY;
    public final float m_rotationZ;
    
    public ParticleModel(final int bitmapId, final float hotX, final float hotY, final float scaleX, final float scaleY, final float scaleRandomX, final float scaleRandomY, final boolean scaleRandomKeepRatio, final float rotation, final float rotationRandom, final float redColor, final float greenColor, final float blueColor, final float alphaColor, final float redColorRandom, final float greenColorRandom, final float blueColorRandom, final float alphaColorRandom, final float rotationX, final float rotationY, final float rotationZ) {
        super();
        this.m_hotX = hotX;
        this.m_hotY = hotY;
        this.m_scaleX = scaleX;
        this.m_scaleY = scaleY;
        this.m_scaleRandomKeepRatio = scaleRandomKeepRatio;
        this.m_scaleRandomX = scaleRandomX;
        this.m_scaleRandomY = scaleRandomY;
        this.m_rotation = rotation;
        this.m_rotationRandom = rotationRandom;
        this.m_bitmapId = bitmapId;
        this.m_redColor = redColor;
        this.m_greenColor = greenColor;
        this.m_blueColor = blueColor;
        this.m_alphaColor = alphaColor;
        this.m_redColorRandom = redColorRandom;
        this.m_greenColorRandom = greenColorRandom;
        this.m_blueColorRandom = blueColorRandom;
        this.m_alphaColorRandom = alphaColorRandom;
        this.m_textureNeedUpdate = true;
        this.m_rotationX = rotationX;
        this.m_rotationY = rotationY;
        this.m_rotationZ = rotationZ;
    }
    
    public Particle generateParticle(final ParticleSystem particleSystem) {
        final Particle particle = Particle.Factory.newPooledInstance();
        particle.m_model = this;
        return particle;
    }
    
    public void initializeParticle(final Particle particle) {
        float scaleX = this.m_scaleX;
        float scaleY = this.m_scaleY;
        float rotation = this.m_rotation;
        if (this.m_scaleRandomKeepRatio) {
            final float randomScale = MathHelper.randomFloat() * this.m_scaleRandomX;
            scaleX += randomScale;
            scaleY += randomScale;
        }
        else {
            if (this.m_scaleRandomX != 0.0f) {
                scaleX += MathHelper.randomFloat() * this.m_scaleRandomX;
            }
            if (this.m_scaleRandomY != 0.0f) {
                scaleY += MathHelper.randomFloat() * this.m_scaleRandomY;
            }
        }
        if (this.m_rotationRandom != 0.0f) {
            rotation += (MathHelper.randomFloat() - 0.5f) * this.m_rotationRandom;
        }
        particle.m_hotX = this.m_hotX;
        particle.m_hotY = this.m_hotY;
        particle.m_alpha = this.m_alphaColor + MathHelper.randomFloat() * this.m_alphaColorRandom;
        particle.m_red = this.m_redColor + MathHelper.randomFloat() * this.m_redColorRandom;
        particle.m_green = this.m_greenColor + MathHelper.randomFloat() * this.m_greenColorRandom;
        particle.m_blue = this.m_blueColor + MathHelper.randomFloat() * this.m_blueColorRandom;
        particle.m_scaleX = scaleX;
        particle.m_scaleY = scaleY;
        particle.m_angle = rotation * 0.017453292f;
        particle.m_halfWidth = this.m_halfWidth;
        particle.m_halfHeight = this.m_halfHeight;
        particle.m_textureTop = this.m_textureTop;
        particle.m_textureLeft = this.m_textureLeft;
        particle.m_textureBottom = this.m_textureBottom;
        particle.m_textureRight = this.m_textureRight;
        final float n = this.m_rotationX * 0.017453292f;
        particle.m_angleX = n;
        particle.m_baseAngleX = n;
        final float n2 = this.m_rotationY * 0.017453292f;
        particle.m_angleY = n2;
        particle.m_baseAngleY = n2;
        final float n3 = this.m_rotationZ * 0.017453292f;
        particle.m_angleZ = n3;
        particle.m_baseAngleZ = n3;
    }
    
    public void updateParticle(final Particle particle) {
    }
    
    public abstract boolean isSequence();
    
    public final int getTextureIndex() {
        return this.m_textureIndex;
    }
    
    public final void setTextureIndex(final int textureIndex) {
        this.m_textureIndex = textureIndex;
    }
    
    public void setTextureCoords(final float top, final float left, final float bottom, final float right) {
        this.m_textureTop = top;
        this.m_textureLeft = left;
        this.m_textureBottom = bottom;
        this.m_textureRight = right;
    }
    
    public void setHalfSize(final float halfWidth, final float halfHeight) {
        this.m_halfWidth = halfWidth;
        this.m_halfHeight = halfHeight;
    }
    
    public int getBitmapId() {
        return this.m_bitmapId;
    }
    
    public void setBitmapId(final int bitmapId) {
        this.m_bitmapId = bitmapId;
        this.m_textureNeedUpdate = true;
    }
}
