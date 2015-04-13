package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class LightRadiusDeformer extends Affector
{
    public final float m_growthX;
    
    public LightRadiusDeformer(final float growthX) {
        super();
        this.m_growthX = growthX;
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        target.m_scaleX += this.m_growthX * (int)(33333.0f * timeIncrement) / 1000.0f;
    }
}
