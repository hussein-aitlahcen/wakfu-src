package com.ankamagames.wakfu.client.core.game.ressource;

import com.ankamagames.xulor2.component.*;

public class LinearActionProgression extends ActionProgression
{
    private final double m_progressSpeed;
    
    public LinearActionProgression(final boolean progressBarReversed, final long startTime, final double initialProgress, final double progressSpeed) {
        this(progressBarReversed, startTime, initialProgress, progressSpeed, false);
    }
    
    public LinearActionProgression(final boolean progressBarReversed, final long startTime, final double initialProgress, final double progressSpeed, final boolean autoEndAction) {
        super(progressBarReversed, startTime, initialProgress, autoEndAction);
        this.m_progressSpeed = progressSpeed;
    }
    
    @Override
    public double getActualProgress(final long currentTime) {
        return this.m_initialProgress + (currentTime - this.m_startTime) * this.m_progressSpeed;
    }
    
    @Override
    public void onUpdate(final long currentTime, final ProgressBar... bar) {
    }
    
    @Override
    public void start(final ProgressBar... bars) {
        final long duration = (long)((1.0 - this.m_initialProgress) / this.m_progressSpeed);
        for (final ProgressBar bar : bars) {
            bar.setInitialValue(this.m_progressBarReversed ? (1.0f - (float)this.m_initialProgress) : ((float)this.m_initialProgress));
            bar.setTweenDuration(duration);
            bar.setValue(this.m_progressBarReversed ? 0.0f : 1.0f);
        }
    }
}
