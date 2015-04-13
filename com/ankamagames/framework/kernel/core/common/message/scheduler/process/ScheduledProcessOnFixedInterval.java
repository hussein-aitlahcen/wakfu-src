package com.ankamagames.framework.kernel.core.common.message.scheduler.process;

class ScheduledProcessOnFixedInterval extends ScheduledProcess
{
    protected long m_rescheduleDelay;
    
    ScheduledProcessOnFixedInterval(final Runnable runnable, final long rescheduleDelay) {
        super(runnable);
        this.m_rescheduleDelay = rescheduleDelay;
    }
    
    @Override
    public long computeNextSchedulingTime(final long referenceTime) {
        return this.m_nextSchedulingTime = referenceTime + this.m_rescheduleDelay;
    }
}
