package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class Deformer extends Affector
{
    public final float m_growthX;
    public final float m_growthY;
    public final float m_angle;
    public final float m_growthRandomX;
    public final float m_growthRandomY;
    public final float m_angleRandom;
    
    public Deformer(final float growthX, final float growthY, final float angle, final float growthRandomX, final float growthRandomY, final float angleRandom) {
        super();
        this.m_growthX = growthX;
        this.m_growthY = growthY;
        this.m_angle = angle;
        this.m_growthRandomX = growthRandomX;
        this.m_growthRandomY = growthRandomY;
        this.m_angleRandom = angleRandom;
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        final float t = (int)(33333.0f * timeIncrement) / 1000.0f;
        target.m_scaleX += this.m_growthX * t;
        target.m_scaleY += this.m_growthY * t;
        target.m_angle += this.m_angle * t;
    }
    
    public static class Random extends Deformer
    {
        public Random(final float growthX, final float growthY, final float angle, final float growthRandomX, final float growthRandomY, final float angleRandom) {
            super(growthX, growthY, angle, growthRandomX, growthRandomY, angleRandom);
        }
        
        @Override
        public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
            target.m_scaleX += this.m_growthX + MathHelper.randomFloat() * this.m_growthRandomX;
            target.m_scaleY += this.m_growthY + MathHelper.randomFloat() * this.m_growthRandomY;
            target.m_angle += this.m_angle + MathHelper.randomFloat() * this.m_angleRandom;
        }
    }
}
