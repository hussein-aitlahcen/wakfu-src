package com.ankamagames.framework.graphics.engine.particleSystem;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.particleSystem.conditions.*;
import com.ankamagames.framework.graphics.engine.particleSystem.affectors.AffectorValidator.*;

public abstract class Affector
{
    private static final Logger m_logger;
    private static final AffectorValidator ALWAYS;
    AffectorValidator m_validator;
    
    public void setValidator(final AffectorValidator validator) {
        this.m_validator = validator;
    }
    
    public void addCondition(final AffectorCondition[] condition) {
        if (condition == null) {
            this.m_validator = Affector.ALWAYS;
        }
        else {
            this.m_validator = null;
            float start = 0.0f;
            float end = Float.POSITIVE_INFINITY;
            PositionCondition c = null;
            for (int i = 0; i < condition.length; ++i) {
                if (condition[i] instanceof PositionCondition) {
                    if (c != null) {
                        Affector.m_logger.error((Object)"pas cool");
                    }
                    c = (PositionCondition)condition[i];
                }
                else {
                    final LifeCondition l = (LifeCondition)condition[i];
                    start = l.m_start;
                    end = l.m_end;
                    if (end <= start) {
                        end = Float.POSITIVE_INFINITY;
                    }
                }
            }
            if (c != null) {
                this.m_validator = new ConditionValidator(start, end, new PositionCondition[] { c });
            }
            else if (end != Float.POSITIVE_INFINITY) {
                this.m_validator = new TimeValidator(start, end);
            }
            else {
                this.m_validator = Affector.ALWAYS;
            }
        }
    }
    
    public final boolean update(final float timeIncrement, final Particle reference, final Particle target, final ParticleSystem particleSystem) {
        return this.m_validator.update(this, reference, target, timeIncrement, particleSystem);
    }
    
    public boolean isKeyFramedAffector() {
        return false;
    }
    
    public abstract void affect(final float p0, final float p1, final Particle p2, final Particle p3, final ParticleSystem p4);
    
    static {
        m_logger = Logger.getLogger((Class)Affector.class);
        ALWAYS = new AffectorValidator() {
            @Override
            public boolean update(final Affector affector, final Particle reference, final Particle target, final float timeIncrement, final ParticleSystem particleSystem) {
                final float elapsedTime = target.m_life;
                final float particleLifeTime = target.m_lifeTime;
                final float timeProgressRatio = elapsedTime / particleLifeTime;
                affector.affect(timeIncrement, timeProgressRatio, reference, target, particleSystem);
                return false;
            }
        };
    }
}
