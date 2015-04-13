package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class RotationInterpolation extends Affector
{
    public final float m_angleX;
    public final float m_angleY;
    public final float m_angleZ;
    
    public RotationInterpolation(final float angleX, final float angleY, final float angleZ) {
        super();
        this.m_angleX = angleX;
        this.m_angleY = angleY;
        this.m_angleZ = angleZ;
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        target.m_angleX = target.m_baseAngleX + this.m_angleX * timeProgressRatio;
        target.m_angleY = target.m_baseAngleY + this.m_angleY * timeProgressRatio;
        target.m_angleZ = target.m_baseAngleZ + this.m_angleZ * timeProgressRatio;
    }
}
