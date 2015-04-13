package com.ankamagames.baseImpl.graphics.alea.display.effects.particles.rain;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class RainParticleEffect extends FullScreenParticleEffect
{
    private float m_particleLifeTime;
    private float m_particleHeight;
    private float m_particleVelocity;
    private float m_particleAcceleration;
    private float m_particleAngle;
    private float m_particleAngleRandom;
    private String m_textureName;
    private float m_r;
    private float m_g;
    private float m_b;
    private float m_a;
    private float m_randA;
    
    public RainParticleEffect() {
        super(new RainParticleFactory());
        this.m_r = 1.0f;
        this.m_g = 1.0f;
        this.m_b = 1.0f;
        this.m_a = 0.1f;
        this.m_randA = 0.0f;
        this.m_numParticlePerSpawn = 128.0f;
        this.m_spawnFrequency = 100.0f;
        this.m_particleLifeTime = 300.0f;
        this.m_a = 0.25f;
        this.m_particleHeight = 40.0f;
        this.m_particleVelocity = 60.0f;
        this.m_particleAcceleration = 0.0f;
        this.m_particleAngle = -1.5707964f;
        this.m_particleAngleRandom = 0.31415927f;
        this.m_textureName = "textures/rain.tga";
    }
    
    public void start(final float numParticlePerSpawn, final float spawnFrequency, final float lifeTime, final float height, final float velocity, final float acceleration) {
        this.activate(true);
        this.m_numParticlePerSpawn = numParticlePerSpawn;
        this.m_spawnFrequency = spawnFrequency * 0.001f;
        this.m_particleLifeTime = lifeTime * 0.001f;
        this.m_particleHeight = height;
        this.m_particleVelocity = velocity;
        this.m_particleAcceleration = acceleration;
        this.setTexture();
    }
    
    private void setTexture() {
        final Renderer renderer = RendererType.OpenGL.getRenderer();
        final Texture texture = TextureManager.getInstance().createTexture(renderer, -1296775008915292150L, Engine.getInstance().getEffectPath() + this.m_textureName, false);
        this.m_entity.setTexture(0, texture);
    }
    
    public final void setColor(final float r, final float g, final float b, final float a, final float randA) {
        this.m_r = r;
        this.m_g = g;
        this.m_b = b;
        this.m_a = a;
        this.m_randA = randA;
    }
    
    public void setParticleAcceleration(final float particleAcceleration) {
        this.m_particleAcceleration = particleAcceleration;
    }
    
    public final void setAngle(final float angle, final float randAngle) {
        this.m_particleAngle = angle;
        this.m_particleAngleRandom = randAngle;
    }
    
    @Override
    protected boolean useRotation() {
        return true;
    }
    
    @Override
    protected boolean useZoomRatio() {
        return true;
    }
    
    @Override
    protected void initializeParticle(final Particle particle) {
        final float r = MathHelper.randomFloat();
        final float min = 1.0f;
        final float max = 10.0f;
        final float p = 1.0f + MathHelper.sinf(r * r * 0.7853982f) * 9.0f;
        final float random = MathHelper.random(1.0f, p);
        final float coeff = (random - 1.0f) / 9.0f;
        final float positionX = this.m_camera.getCameraExactIsoWorldX();
        final float positionY = this.m_camera.getCameraExactIsoWorldY();
        final float positionZ = this.m_camera.getAltitude();
        particle.m_x = this.m_box.getxMin() + MathHelper.randomFloat() * this.m_box.width() + positionX;
        particle.m_y = this.m_box.getyMin() + MathHelper.randomFloat() * this.m_box.height() + positionY;
        particle.m_z = this.m_box.getzMax() + positionZ;
        particle.m_velocityX = 0.0f;
        particle.m_velocityY = 0.0f;
        particle.m_velocityZ = 0.1f * this.m_particleVelocity * (1.0f + coeff);
        particle.m_accelerationX = 0.0f;
        particle.m_accelerationY = 0.0f;
        particle.m_accelerationZ = this.m_particleAcceleration;
        particle.m_height = this.m_particleHeight * (2.0f + 2.0f * coeff);
        particle.m_width = coeff * 0.5f + 0.9f;
        particle.m_lifeTime = this.m_particleLifeTime / (1.0f + coeff);
        particle.m_life = 0.0f;
        particle.m_red = this.m_r;
        particle.m_green = this.m_g;
        particle.m_blue = this.m_b;
        particle.m_alpha = this.m_a + coeff * this.m_randA;
        particle.m_top = 0.0f;
        particle.m_left = 0.0f;
        particle.m_bottom = 1.0f;
        particle.m_right = 1.0f;
        particle.m_angle = this.m_particleAngle + MathHelper.random(-1.0f, 1.0f) * this.m_particleAngleRandom;
        this.m_entity.setPreRenderStates(new RenderStates() {
            @Override
            public void apply(final Renderer renderer) {
                RenderStateManager.getInstance().setBlendFunc(BlendModes.SrcAlpha, BlendModes.InvSrcAlpha);
                super.apply(renderer);
            }
        });
    }
    
    @Override
    protected final void updateParticle(final Particle particle, final float timeIncrement) {
        particle.update(timeIncrement);
    }
}
