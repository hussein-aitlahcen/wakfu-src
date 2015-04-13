package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class Rotation extends Affector
{
    public final float m_angleX;
    public final float m_angleY;
    public final float m_angleZ;
    
    public Rotation(final float angleX, final float angleY, final float angleZ) {
        super();
        this.m_angleX = angleX;
        this.m_angleY = angleY;
        this.m_angleZ = angleZ;
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        final float t = (int)(33333.0f * timeIncrement) / 1000.0f;
        target.m_angleX += this.m_angleX * t;
        target.m_angleY += this.m_angleY * t;
        target.m_angleZ += this.m_angleZ * t;
    }
}
