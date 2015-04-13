package com.ankamagames.framework.graphics.engine.particleSystem;

import com.ankamagames.framework.graphics.engine.light.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class Light
{
    public static final int FADE_OUT_TIME = 100;
    private final LightSource m_light;
    
    public Light() {
        super();
        (this.m_light = LightSourceManagerDelegate.INSTANCE.createLightSource()).setEnabled(false);
        assert this.m_light != null;
    }
    
    public void update(final float timeIncrement, final Emitter emitter, final ParticleSystem particleSystem) {
        final Particle particle = emitter.m_lightParticle;
        particle.m_parent = emitter.m_parent;
        final float life = particle.m_life - emitter.m_definition.m_startSpawnTime;
        this.m_light.setEnabled(life > 0.0f);
        particle.update(particleSystem, timeIncrement);
        this.m_light.getPosition().set(particle.getX() + particleSystem.getX(), particle.getY() + particleSystem.getY(), particle.getZ() + particleSystem.getZ());
        final float intensityRatio = particle.m_alpha;
        this.m_light.setBaseColor(particle.m_red * intensityRatio, particle.m_green * intensityRatio, particle.m_blue * intensityRatio);
        this.m_light.setSaturation(particle.m_red * intensityRatio, particle.m_green * intensityRatio, particle.m_blue * intensityRatio);
        this.m_light.setRange(MathHelper.clamp(particle.m_halfWidth * particle.m_scaleX, 0.0f, 5.0f));
        if (life >= particle.m_lifeTime && particle.m_lifeTime != Float.MAX_VALUE) {
            this.unregister();
        }
    }
    
    public void setColor(final float red, final float green, final float blue) {
        this.m_light.setSaturation(red, green, blue);
    }
    
    public void setRange(final float range) {
        this.m_light.setRange(range);
    }
    
    public void register() {
        LightSourceManagerDelegate.INSTANCE.addLight(this.m_light);
    }
    
    public void unregister() {
        this.m_light.shutdown(100);
    }
}
