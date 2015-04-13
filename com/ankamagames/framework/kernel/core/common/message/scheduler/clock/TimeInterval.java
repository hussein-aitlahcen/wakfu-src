package com.ankamagames.framework.kernel.core.common.message.scheduler.clock;

public enum TimeInterval
{
    SECONDS(1000L), 
    MINUTES(60000L), 
    HOURS(3600000L), 
    DAYS(86400000L);
    
    private final long m_msInInterval;
    
    private TimeInterval(final long msInInterval) {
        this.m_msInInterval = msInInterval;
    }
    
    public long toMillis(final long intervalCount) {
        return this.m_msInInterval * intervalCount;
    }
}
