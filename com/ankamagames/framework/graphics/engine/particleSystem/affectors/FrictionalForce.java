package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class FrictionalForce extends Affector
{
    public final float m_friction;
    
    public FrictionalForce(final float friction) {
        super();
        this.m_friction = friction;
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        final float d = 1.0f - this.m_friction * timeIncrement;
        target.m_velocityX *= d;
        target.m_velocityY *= d;
        target.m_velocityZ *= d;
    }
}
