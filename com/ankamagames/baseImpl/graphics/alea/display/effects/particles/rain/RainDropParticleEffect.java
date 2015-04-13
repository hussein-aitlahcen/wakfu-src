package com.ankamagames.baseImpl.graphics.alea.display.effects.particles.rain;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class RainDropParticleEffect extends FullScreenParticleEffect
{
    public RainDropParticleEffect() {
        super(new RainDropParticleFactory());
        this.m_numParticlePerSpawn = 1.0f;
        this.m_spawnFrequency = 0.0f;
    }
    
    public void start(final float numParticlePerSpawn, final float spawnFrequency) {
        this.activate(true);
        this.m_numParticlePerSpawn = numParticlePerSpawn;
        this.m_spawnFrequency = spawnFrequency * 0.001f;
        final Renderer renderer = RendererType.OpenGL.getRenderer();
        final Texture texture = TextureManager.getInstance().createTexture(renderer, -1296775008915292148L, Engine.getInstance().getEffectPath() + "textures/raindrop.tga", false);
        this.m_entity.setTexture(0, texture);
    }
    
    @Override
    protected boolean useRotation() {
        return false;
    }
    
    @Override
    protected boolean useZoomRatio() {
        return true;
    }
    
    @Override
    protected void initializeParticle(final Particle particle) {
        final float positionX = this.m_camera.getCameraExactIsoWorldX();
        final float positionY = this.m_camera.getCameraExactIsoWorldY();
        final float positionZ = this.m_camera.getAltitude();
        particle.m_x = positionX + MathHelper.random(this.m_box.getxMin(), this.m_box.getxMax());
        particle.m_y = positionY + MathHelper.random(this.m_box.getyMin(), this.m_box.getyMax());
        particle.m_z = positionZ + this.m_box.getzMin();
        particle.m_lifeTime = 0.33f;
        particle.m_life = 0.0f;
        particle.m_red = 1.0f;
        particle.m_green = 1.0f;
        particle.m_blue = 1.0f;
        particle.m_alpha = 1.0f;
        particle.m_top = 0.0f;
        particle.m_left = 0.0f;
        particle.m_bottom = 1.0f;
        particle.m_right = 1.0f;
        final float r = MathHelper.random(7.0f, 11.0f);
        particle.m_width = r * 2.0f;
        particle.m_height = r;
    }
    
    @Override
    protected void updateParticle(final Particle particle, final float timeIncrement) {
        particle.update(timeIncrement);
    }
}
