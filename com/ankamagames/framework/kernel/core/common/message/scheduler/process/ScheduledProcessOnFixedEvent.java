package com.ankamagames.framework.kernel.core.common.message.scheduler.process;

import java.util.*;

class ScheduledProcessOnFixedEvent extends ScheduledProcess
{
    private final long m_timeEventModulo;
    
    ScheduledProcessOnFixedEvent(final Runnable runnable, final long timeEventModulo) {
        super(runnable);
        this.m_timeEventModulo = timeEventModulo;
    }
    
    @Override
    public long computeNextSchedulingTime(final long referenceTime) {
        final long timeZoneOffset = Calendar.getInstance().getTimeZone().getOffset(referenceTime);
        final long eventsCount = (referenceTime + timeZoneOffset) / this.m_timeEventModulo;
        return this.m_nextSchedulingTime = (1L + eventsCount) * this.m_timeEventModulo - timeZoneOffset;
    }
    
    @Override
    public String toString() {
        return "ScheduledProcessOnFixedEvent{m_timeEventModulo=" + this.m_timeEventModulo + '}';
    }
}
