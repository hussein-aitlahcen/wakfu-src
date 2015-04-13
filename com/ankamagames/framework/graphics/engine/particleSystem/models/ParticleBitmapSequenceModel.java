package com.ankamagames.framework.graphics.engine.particleSystem.models;

import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.framework.graphics.image.*;
import com.sun.opengl.util.texture.*;

public class ParticleBitmapSequenceModel extends ParticleModel
{
    public float m_speed;
    public int m_loopCount;
    public final AnimData m_animData;
    private float m_currentTime;
    
    public ParticleBitmapSequenceModel(final int bitmapId, final float hotX, final float hotY, final float scaleX, final float scaleY, final float scaleRandomX, final float scaleRandomY, final boolean scaleRandomKeepRatio, final float rotation, final float rotationRandom, final float redColor, final float greenColor, final float blueColor, final float alphaColor, final float redColorRandom, final float greenColorRandom, final float blueColorRandom, final float alphaColorRandom, final AnimData animData, final float speed, final int loopCount, final float rotationX, final float rotationY, final float rotationZ) {
        super(bitmapId, hotX, hotY, scaleX, scaleY, scaleRandomX, scaleRandomY, scaleRandomKeepRatio, rotation, rotationRandom, redColor, greenColor, blueColor, alphaColor, redColorRandom, greenColorRandom, blueColorRandom, alphaColorRandom, rotationX, rotationY, rotationZ);
        this.m_speed = 1.0f;
        this.m_loopCount = -1;
        this.m_animData = animData;
        this.m_speed = speed;
        this.m_loopCount = loopCount;
    }
    
    @Override
    public Particle generateParticle(final ParticleSystem particleSystem) {
        if (particleSystem.isEditable() && !this.updateTexture(particleSystem)) {
            return null;
        }
        return super.generateParticle(particleSystem);
    }
    
    @Override
    public boolean isSequence() {
        return true;
    }
    
    public boolean updateTexture(final ParticleSystem particleSystem) {
        assert this.m_textureNeedUpdate : "Texture is already up to date";
        assert particleSystem.isEditable();
        final AlphaBitmapData image = particleSystem.getEditableData().getBitmap(this.getBitmapId());
        if (image == null) {
            return false;
        }
        this.m_textureNeedUpdate = false;
        return true;
    }
    
    public final TextureCoords getTextureCoordinates(final int elapsedTime) {
        assert elapsedTime >= 0;
        this.m_currentTime += this.m_speed * elapsedTime;
        final int totalTime = this.m_animData.getTotalTime();
        if (this.m_currentTime >= totalTime) {
            this.m_currentTime -= totalTime;
            if (this.m_loopCount > 0) {
                --this.m_loopCount;
            }
        }
        if (this.m_loopCount == 0) {
            return this.m_animData.getTextureCoordinates((short)totalTime);
        }
        return this.m_animData.getTextureCoordinates((short)this.m_currentTime);
    }
}
