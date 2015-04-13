package com.ankamagames.baseImpl.graphics.alea.display.effects.particles.snow;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class SnowParticleEffect extends FullScreenParticleEffect
{
    private float m_particleLifeTime;
    private float m_particleSize;
    private float m_wind;
    private float m_mass;
    private float m_massRandom;
    private float m_accelerationZ;
    private float m_r;
    private float m_g;
    private float m_b;
    private float m_a;
    private float m_randA;
    
    public SnowParticleEffect() {
        super(new SnowParticleFactory());
        this.m_r = 1.0f;
        this.m_g = 1.0f;
        this.m_b = 1.0f;
        this.m_a = 0.6f;
        this.m_randA = 0.0f;
        this.m_numParticlePerSpawn = 10.0f;
        this.m_spawnFrequency = 0.3f;
        this.m_particleLifeTime = 15.0f;
        this.m_particleSize = 1.0f;
        this.m_mass = 0.5f;
        this.m_massRandom = 0.5f;
        this.m_accelerationZ = -3.0f;
    }
    
    public void start(final float numParticlePerSpawn, final float spawnFrequency, final float lifeTime, final float size) {
        this.activate(true);
        this.m_numParticlePerSpawn = numParticlePerSpawn;
        this.m_spawnFrequency = spawnFrequency * 0.001f;
        this.m_particleLifeTime = lifeTime * 0.001f;
        this.m_particleSize = size;
        this.setTexture("textures/snow.tga");
    }
    
    public void setTexture(String textureName) {
        textureName = "textures/snow.tga";
        final Renderer renderer = RendererType.OpenGL.getRenderer();
        final Texture texture = TextureManager.getInstance().createTexture(renderer, -1296775008915292151L, Engine.getInstance().getEffectPath() + textureName, false);
        this.m_entity.setTexture(0, texture);
    }
    
    public final void setColor(final float r, final float g, final float b, final float a, final float randA) {
        this.m_r = r;
        this.m_g = g;
        this.m_b = b;
        this.m_a = a;
        this.m_randA = randA;
    }
    
    public void setAccelerationZ(final float acceleration) {
        this.m_accelerationZ = acceleration;
    }
    
    public void setWind(final float wind) {
        this.m_wind = wind;
    }
    
    public void setMass(final float mass, final float randomMass) {
        this.m_mass = mass;
        this.m_massRandom = randomMass;
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
        particle.m_x = this.m_box.getxMin() + MathHelper.randomFloat() * this.m_box.width() + positionX;
        particle.m_y = this.m_box.getyMin() + MathHelper.randomFloat() * this.m_box.height() + positionY;
        particle.m_z = this.m_box.getzMax() - MathHelper.randomFloat() * ((this.m_box.getzMax() + this.m_box.getzMin()) * 0.5f) + positionZ;
        particle.m_velocityX = 0.0f;
        particle.m_velocityY = 0.0f;
        particle.m_velocityZ = 0.0f;
        particle.m_lifeTime = this.m_particleLifeTime;
        particle.m_life = 0.0f;
        particle.m_red = this.m_r;
        particle.m_green = this.m_g;
        particle.m_blue = this.m_b;
        particle.m_alpha = this.m_a + MathHelper.random() * this.m_randA;
        particle.m_width = this.m_particleSize + this.m_particleSize * MathHelper.randomFloat() * 1.5f;
        particle.m_height = particle.m_width;
        particle.m_top = 0.0f;
        particle.m_left = 0.0f;
        particle.m_bottom = 1.0f;
        particle.m_right = 1.0f;
        final SnowParticle sp = (SnowParticle)particle;
        sp.m_mass = this.m_mass + MathHelper.randomFloat() * this.m_massRandom;
        sp.m_floorContact = false;
        sp.m_wind = this.m_wind;
        sp.m_zAcc = this.m_accelerationZ;
    }
    
    @Override
    protected final void updateParticle(final Particle particle, final float timeIncrement) {
        final SnowParticle sp = (SnowParticle)particle;
        if (particle.m_z <= this.m_camera.getAltitude() + 0.1f) {
            sp.m_floorContact = true;
        }
        final SnowParticle snowParticle = sp;
        snowParticle.m_wind += this.m_wind;
        particle.update(timeIncrement);
    }
}
