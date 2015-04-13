package com.ankamagames.framework.graphics.engine.particleSystem.affectors.AffectorValidator;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class TimeValidator implements AffectorValidator
{
    protected final float m_start;
    protected final float m_end;
    
    public TimeValidator(final float start, final float end) {
        super();
        this.m_start = start;
        this.m_end = end;
    }
    
    @Override
    public final boolean update(final Affector affector, final Particle reference, final Particle target, final float timeIncrement, final ParticleSystem particleSystem) {
        final float life = target.m_life;
        if (life < this.m_start) {
            return false;
        }
        final float d = this.m_end - life;
        if (d < 0.0f) {
            return true;
        }
        final float elapsedTimeSinceStart = timeIncrement - this.m_start;
        final float validatorTotalTime = this.m_end - this.m_start;
        final float timeProgressRatio = Math.max(0.0f, elapsedTimeSinceStart / validatorTotalTime);
        affector.affect((d < timeIncrement) ? d : timeIncrement, timeProgressRatio, reference, target, particleSystem);
        return false;
    }
}
