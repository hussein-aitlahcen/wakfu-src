package com.ankamagames.framework.graphics.engine.particleSystem.models;

import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.image.*;

public class ParticleBitmapModel extends ParticleModel
{
    public ParticleBitmapModel(final int bitmapId, final float hotX, final float hotY, final float scaleX, final float scaleY, final float scaleRandomX, final float scaleRandomY, final boolean scaleRandomKeepRatio, final float rotation, final float rotationRandom, final float redColor, final float greenColor, final float blueColor, final float alphaColor, final float redColorRandom, final float greenColorRandom, final float blueColorRandom, final float alphaColorRandom, final float rotationX, final float rotationY, final float rotationZ) {
        super(bitmapId, hotX, hotY, scaleX, scaleY, scaleRandomX, scaleRandomY, scaleRandomKeepRatio, rotation, rotationRandom, redColor, greenColor, blueColor, alphaColor, redColorRandom, greenColorRandom, blueColorRandom, alphaColorRandom, rotationX, rotationY, rotationZ);
    }
    
    @Override
    public Particle generateParticle(final ParticleSystem particleSystem) {
        if (particleSystem.isEditable() && this.m_textureNeedUpdate && !this.updateTexture(particleSystem)) {
            return null;
        }
        return super.generateParticle(particleSystem);
    }
    
    @Override
    public boolean isSequence() {
        return false;
    }
    
    public boolean updateTexture(final ParticleSystem particleSystem) {
        assert this.m_textureNeedUpdate : "Texture is already up to date";
        assert particleSystem.isEditable();
        final AlphaBitmapData image = particleSystem.getEditableData().getBitmap(this.getBitmapId());
        if (image == null) {
            return false;
        }
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
        final int textureWidth = MathHelper.nearestGreatestPowOfTwo(imageWidth);
        final int textureHeight = MathHelper.nearestGreatestPowOfTwo(imageHeight);
        this.m_halfWidth = imageWidth * 0.5f;
        this.m_halfHeight = imageHeight * 0.5f;
        this.m_textureBottom = imageHeight / textureHeight;
        this.m_textureRight = imageWidth / textureWidth;
        this.m_textureNeedUpdate = false;
        return true;
    }
}
