package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class BoostForce extends Affector
{
    public final float m_x;
    public final float m_y;
    public final float m_z;
    
    public BoostForce(final float x, final float y, final float z) {
        super();
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        target.m_velocityX += this.m_x;
        target.m_velocityY += this.m_y;
        target.m_velocityZ += this.m_z;
    }
}
