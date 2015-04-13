package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.framework.kernel.core.maths.*;

class FloatParameter
{
    private float m_current;
    private float m_previous;
    private float m_next;
    private long m_changeStartTime;
    private long m_changeDuration;
    
    FloatParameter(final float current) {
        super();
        this.m_current = current;
        this.m_next = current;
        this.m_previous = current;
        this.m_changeStartTime = 0L;
        this.m_changeDuration = 0L;
    }
    
    void set(final float value) {
        this.m_current = value;
        this.m_next = value;
        this.m_previous = value;
        this.m_changeStartTime = 0L;
        this.m_changeDuration = 0L;
    }
    
    void changeTo(final float target, final long duration, final long currentTime) {
        this.m_next = target;
        this.m_previous = this.m_current;
        this.m_changeDuration = Math.max(duration, 1L);
        this.m_changeStartTime = currentTime;
    }
    
    float getCurrent() {
        return this.m_current;
    }
    
    public float getNext() {
        return this.m_next;
    }
    
    boolean isOvertime(final long currentTime) {
        return currentTime > this.m_changeStartTime + this.m_changeDuration;
    }
    
    void update(final long currentTime) {
        final long deltaTime = currentTime - this.m_changeStartTime;
        if (deltaTime < this.m_changeDuration) {
            final float a = deltaTime / this.m_changeDuration;
            this.m_current = MathHelper.lerp(this.m_previous, this.m_next, a);
        }
        else {
            this.m_current = this.m_next;
        }
    }
}
