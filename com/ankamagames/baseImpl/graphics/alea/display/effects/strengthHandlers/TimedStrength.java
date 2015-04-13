package com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers;

import com.ankamagames.framework.kernel.core.maths.*;

public class TimedStrength extends StrengthHandler
{
    private final int m_startDuration;
    private final int m_middleDuration;
    private final int m_endDuration;
    private int m_elapsedTime;
    
    public TimedStrength(final int startDuration, final int middleDuration, final int endDuration) {
        super(1.0f);
        this.m_startDuration = startDuration;
        this.m_middleDuration = middleDuration;
        this.m_endDuration = endDuration;
    }
    
    public TimedStrength(final int startDuration, final int middleDuration, final int endDuration, final float startStrength) {
        this(startDuration, middleDuration, endDuration);
        assert startStrength >= 0.0f && startStrength <= 1.0f;
        this.m_elapsedTime = (int)MathHelper.lerp(0.0f, startDuration, startStrength);
    }
    
    @Override
    public boolean update(final int deltaTime) {
        this.m_elapsedTime += deltaTime;
        return this.m_elapsedTime <= this.m_startDuration + this.m_middleDuration + this.m_endDuration && super.update(deltaTime);
    }
    
    @Override
    public float getStrength() {
        final float s = MathHelper.clamp(this.getForceAtTime(), 0.0f, 1.0f);
        return super.getStrength() * s;
    }
    
    private float getForceAtTime() {
        float time = this.m_elapsedTime;
        if (time < this.m_startDuration) {
            return time / this.m_startDuration;
        }
        time -= this.m_startDuration;
        if (time < this.m_middleDuration) {
            return 1.0f;
        }
        time -= this.m_middleDuration;
        if (time < this.m_endDuration) {
            return 1.0f - time / this.m_endDuration;
        }
        return 0.0f;
    }
}
