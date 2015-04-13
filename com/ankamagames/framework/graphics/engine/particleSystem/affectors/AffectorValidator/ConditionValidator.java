package com.ankamagames.framework.graphics.engine.particleSystem.affectors.AffectorValidator;

import com.ankamagames.framework.graphics.engine.particleSystem.conditions.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class ConditionValidator implements AffectorValidator
{
    protected final float m_start;
    protected final float m_end;
    private final PositionCondition[] m_condition;
    
    public ConditionValidator(final float start, final float end, final PositionCondition... condition) {
        super();
        this.m_start = start;
        this.m_end = end;
        this.m_condition = condition;
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
        for (int i = 0; i < this.m_condition.length; ++i) {
            if (!this.m_condition[i].validate(reference, target, timeIncrement, particleSystem)) {
                return false;
            }
        }
        final float elapsedTimeSinceStart = timeIncrement - this.m_start;
        final float validatorTotalTime = this.m_end - this.m_start;
        final float timeProgressRatio = Math.max(0.0f, elapsedTimeSinceStart / validatorTotalTime);
        affector.affect((d < timeIncrement) ? d : timeIncrement, timeProgressRatio, reference, target, particleSystem);
        return false;
    }
}
