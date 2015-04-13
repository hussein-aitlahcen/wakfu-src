package com.ankamagames.framework.kernel.core.common.message.scheduler.process;

abstract class ScheduledProcess
{
    protected final Runnable m_runnable;
    protected long m_nextSchedulingTime;
    protected int m_repeatCountsLeft;
    
    protected ScheduledProcess(final Runnable runnable) {
        super();
        this.m_runnable = runnable;
    }
    
    public abstract long computeNextSchedulingTime(final long p0);
    
    public long getNextSchedulingTime() {
        return this.m_nextSchedulingTime;
    }
    
    public Runnable getRunnable() {
        return this.m_runnable;
    }
    
    public int getRepeatCountsLeft() {
        return this.m_repeatCountsLeft;
    }
    
    public void setRepeatCountsLeft(final int repeatCountsLeft) {
        this.m_repeatCountsLeft = repeatCountsLeft;
    }
}
