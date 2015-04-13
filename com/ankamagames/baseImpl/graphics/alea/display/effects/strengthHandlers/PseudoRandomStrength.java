package com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers;

import com.ankamagames.baseImpl.graphics.isometric.interpolation.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class PseudoRandomStrength extends StrengthHandler
{
    private final int m_timeBeforeChange;
    private final Interpolator m_interpolator;
    private int m_elapsedTime;
    private final float[] m_randomValues;
    private int m_currentIndex;
    
    public PseudoRandomStrength(final float[] randomValues, final int timeBeforeChange, final long currentTime) {
        super(1.0f);
        this.m_interpolator = new Interpolator();
        this.m_randomValues = randomValues;
        this.m_timeBeforeChange = timeBeforeChange * 1000;
        this.m_currentIndex = (int)(currentTime / this.m_timeBeforeChange) % this.m_randomValues.length;
        this.m_interpolator.setSpeed(1.0f / (timeBeforeChange * 0.5f));
        this.m_interpolator.set(this.m_randomValues[this.m_currentIndex]);
    }
    
    public PseudoRandomStrength(final int timeBeforeChange, final long currentTime) {
        this(generateValues(100), timeBeforeChange, currentTime);
    }
    
    public static float[] generateValues(final int count) {
        final Distribution d = new Distribution(0.550000011920929, 0.3499999940395355, count);
        final float[] values = new float[count];
        for (int i = 0; i < count; ++i) {
            values[i] = MathHelper.clamp((float)d.nextDouble(), 0.0f, 1.0f);
        }
        return values;
    }
    
    public static float[] generateValues(final int count, final float min, final float max) {
        final Distribution d = new Distribution(0.550000011920929, 0.3499999940395355, count);
        final float[] values = new float[count];
        for (int i = 0; i < count; ++i) {
            values[i] = MathHelper.clamp((float)d.nextDouble(), min, max);
        }
        return values;
    }
    
    @Override
    public boolean update(final int deltaTime) {
        this.m_elapsedTime += deltaTime;
        if (this.m_elapsedTime > this.m_timeBeforeChange) {
            this.m_elapsedTime = 0;
            ++this.m_currentIndex;
            if (this.m_currentIndex >= this.m_randomValues.length) {
                this.m_currentIndex = 0;
            }
            this.m_interpolator.setEnd(this.m_randomValues[this.m_currentIndex]);
        }
        this.m_interpolator.process(deltaTime);
        return super.update(deltaTime);
    }
    
    @Override
    public float getStrength() {
        return super.getStrength() * this.m_interpolator.getValue();
    }
}
