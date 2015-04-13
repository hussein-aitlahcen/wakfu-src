package com.ankamagames.framework.graphics.engine.particleSystem.definitions;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class LightDefinition extends Affectorable
{
    public final float m_red;
    public final float m_green;
    public final float m_blue;
    public final float m_intensity;
    public final float m_range;
    
    public LightDefinition(final float red, final float green, final float blue, final float intensity, final float range) {
        super();
        this.m_red = red;
        this.m_green = green;
        this.m_blue = blue;
        this.m_intensity = intensity;
        this.m_range = range;
    }
    
    public Light createLight() {
        final Light light = new Light();
        light.setColor(this.m_red * this.m_intensity, this.m_green * this.m_intensity, this.m_blue * this.m_intensity);
        light.setRange(this.m_range);
        return light;
    }
    
    public Particle createLightParticle() {
        final Particle particle = Particle.Factory.newPooledInstance();
        particle.m_red = this.m_red;
        particle.m_green = this.m_green;
        particle.m_blue = this.m_blue;
        particle.m_alpha = this.m_intensity;
        particle.m_halfWidth = this.m_range;
        particle.m_halfHeight = this.m_range;
        particle.m_scaleX = 1.0f;
        return particle;
    }
}
