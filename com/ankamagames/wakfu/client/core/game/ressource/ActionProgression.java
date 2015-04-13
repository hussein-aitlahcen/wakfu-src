package com.ankamagames.wakfu.client.core.game.ressource;

import com.ankamagames.xulor2.component.*;

public abstract class ActionProgression
{
    protected final boolean m_progressBarReversed;
    protected final long m_startTime;
    protected final double m_initialProgress;
    protected final boolean m_autoEndAction;
    
    protected ActionProgression(final boolean progressBarReversed, final long startTime, final double initialProgress, final boolean autoEndAction) {
        super();
        this.m_progressBarReversed = progressBarReversed;
        this.m_startTime = startTime;
        this.m_initialProgress = initialProgress;
        this.m_autoEndAction = autoEndAction;
    }
    
    public abstract void start(final ProgressBar... p0);
    
    public abstract void onUpdate(final long p0, final ProgressBar... p1);
    
    public abstract double getActualProgress(final long p0);
}
