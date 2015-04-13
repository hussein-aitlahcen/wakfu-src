package com.ankamagames.framework.graphics.engine.particleSystem.conditions;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class LifeCondition implements AffectorCondition
{
    public final float m_start;
    public final float m_end;
    
    public LifeCondition(final float start, final float end) {
        super();
        this.m_start = start;
        this.m_end = end;
    }
    
    @Override
    public boolean validate(final Particle reference, final Particle target, final float timeIncrement, final ParticleSystem particleSystem) {
        final float life = target.m_life;
        return (life > this.m_end && life - timeIncrement < this.m_start) || (life >= this.m_start && life <= this.m_end);
    }
}
