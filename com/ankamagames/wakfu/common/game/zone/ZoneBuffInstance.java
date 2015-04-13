package com.ankamagames.wakfu.common.game.zone;

import org.apache.log4j.*;

public final class ZoneBuffInstance implements Comparable<ZoneBuffInstance>
{
    protected static final Logger m_logger;
    private final ZoneBuff m_buff;
    private final long m_instantiationTime;
    
    public ZoneBuffInstance(final ZoneBuff buff) {
        super();
        this.m_buff = buff;
        this.m_instantiationTime = System.currentTimeMillis();
    }
    
    public ZoneBuffInstance(final ZoneBuff buff, final int remainingTime) {
        super();
        assert remainingTime <= buff.getDuration() : "Temps restant trop court";
        this.m_buff = buff;
        this.m_instantiationTime = System.currentTimeMillis() + remainingTime - buff.getDuration();
    }
    
    public final ZoneBuff getBuff() {
        return this.m_buff;
    }
    
    public final int getBuffId() {
        return this.m_buff.getId();
    }
    
    public final long getInstantiationTime() {
        return this.m_instantiationTime;
    }
    
    public final long getEndTime() {
        if (this.m_buff.isInfinite()) {
            return Long.MAX_VALUE;
        }
        return this.m_instantiationTime + this.m_buff.getDuration();
    }
    
    public final int getRemainingTime() {
        if (this.m_buff.isInfinite()) {
            return -1;
        }
        final long remaining = this.getEndTime() - System.currentTimeMillis();
        return (remaining <= 2147483647L) ? ((int)remaining) : -1;
    }
    
    @Override
    public final int compareTo(final ZoneBuffInstance o) {
        final long timeDiff = this.getEndTime() - o.getEndTime();
        if (timeDiff > 0L) {
            return 1;
        }
        if (timeDiff < 0L) {
            return -1;
        }
        return 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ZoneBuffInstance.class);
    }
}
