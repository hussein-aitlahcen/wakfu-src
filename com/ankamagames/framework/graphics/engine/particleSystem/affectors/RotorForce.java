package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class RotorForce extends Affector
{
    public final float m_intensity;
    
    public RotorForce(final float intensity) {
        super();
        this.m_intensity = intensity;
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        final float deltaIntensity = this.m_intensity * timeIncrement;
        final Particle ref = (reference.m_parent == null) ? reference : reference.m_parent;
        target.m_x += (target.m_y - ref.getY()) * deltaIntensity;
        target.m_y -= (target.m_x - ref.getX()) * deltaIntensity;
    }
}
