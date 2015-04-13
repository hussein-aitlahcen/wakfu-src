package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class CirclePath extends Affector
{
    public final float m_radialSpeed;
    public float m_grade;
    
    public CirclePath(final float radialSpeed) {
        super();
        this.m_radialSpeed = radialSpeed;
        this.m_grade = 180.0f;
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        this.m_grade += this.m_radialSpeed * timeIncrement;
        if (this.m_grade >= 360.0f) {
            this.m_grade -= 360.0f;
        }
        target.m_x = reference.getX() + MathHelper.cosf(this.m_grade) * target.m_velocityX + 0.4f;
        target.m_y = reference.getY() + MathHelper.sinf(this.m_grade) * target.m_velocityY - 0.7f;
    }
}
