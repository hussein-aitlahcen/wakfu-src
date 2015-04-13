package com.ankamagames.baseImpl.graphics.isometric.interpolation;

import com.ankamagames.framework.kernel.core.maths.*;

public class Interpolator
{
    private int m_elapsedTime;
    private float m_start;
    private float m_end;
    private float m_speed;
    private float m_delta;
    private float m_value;
    
    public void setDelta(final float delta) {
        this.m_delta = delta;
    }
    
    public void setSpeed(final float speed) {
        this.m_speed = speed;
    }
    
    public void set(final float value) {
        this.m_value = value;
        this.m_end = value;
        this.m_start = value;
    }
    
    public float getValue() {
        return this.m_value;
    }
    
    public void setStart(final float start) {
        this.m_start = start;
        this.m_elapsedTime = 0;
    }
    
    public void setEnd(final float end) {
        this.m_end = end;
        this.m_start = this.m_value;
        this.m_elapsedTime = 0;
    }
    
    public float getEnd() {
        return this.m_end;
    }
    
    public float process(final int deltaTime) {
        if (Math.abs(this.m_end - this.m_value) < this.m_delta) {
            this.set(this.m_end);
            return this.m_end;
        }
        this.m_elapsedTime += deltaTime;
        final float f = this.m_elapsedTime * this.m_speed / 1000.0f;
        if (f > 1.0f) {
            this.m_value = this.m_end;
        }
        else {
            final float alpha = MathHelper.sin(f * 1.5707964f);
            this.m_value = this.m_start + (this.m_end - this.m_start) * alpha;
        }
        return this.m_value;
    }
}
