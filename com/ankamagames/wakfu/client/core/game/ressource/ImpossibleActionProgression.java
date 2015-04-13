package com.ankamagames.wakfu.client.core.game.ressource;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.tween.*;

public class ImpossibleActionProgression extends ActionProgression
{
    private static final short TWEEN_DURATION = 500;
    private long m_lastChange;
    private boolean m_direct;
    private float m_visualInitialValue;
    private float m_visualAlternateValue;
    
    public ImpossibleActionProgression(final boolean progressBarReversed, final long startTime, final double initialProgress) {
        super(progressBarReversed, startTime, initialProgress, false);
        this.m_visualInitialValue = (this.m_progressBarReversed ? (1.0f - (float)this.m_initialProgress) : ((float)this.m_initialProgress));
        this.m_visualAlternateValue = MathHelper.clamp(this.m_progressBarReversed ? (this.m_visualInitialValue * 0.9f) : (this.m_visualInitialValue * 1.1f), 0.0f, 1.0f);
    }
    
    @Override
    public void start(final ProgressBar... bars) {
        for (final ProgressBar bar : bars) {
            bar.setTweenDuration(0L);
            bar.setTweenFunction(TweenFunction.PROGRESSIVE);
            bar.setInitialValue(this.m_visualInitialValue);
        }
        this.m_lastChange = this.m_startTime - 500L;
        this.m_direct = true;
    }
    
    @Override
    public void onUpdate(final long currentTime, final ProgressBar... bars) {
        if (this.m_lastChange + 500L <= currentTime) {
            this.m_lastChange += 500L;
            for (final ProgressBar bar : bars) {
                bar.setTweenDuration(Math.max(0L, this.m_lastChange - currentTime + 500L));
                bar.setValue(this.m_direct ? this.m_visualAlternateValue : this.m_visualInitialValue);
            }
            this.m_direct = !this.m_direct;
        }
    }
    
    @Override
    public double getActualProgress(final long currentTime) {
        return this.m_initialProgress;
    }
}
