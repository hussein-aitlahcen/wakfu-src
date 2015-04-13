package com.ankamagames.framework.kernel.core.common.message.scheduler.clock;

import com.ankamagames.framework.kernel.core.common.message.*;

public class ClockMessage extends Message
{
    public static final int ID = Integer.MIN_VALUE;
    private long m_clockId;
    private int m_subId;
    private long m_timeStamp;
    
    public ClockMessage() {
        super();
        this.m_subId = 0;
    }
    
    public long getClockId() {
        return this.m_clockId;
    }
    
    public void setClockId(final long clockId) {
        this.m_clockId = clockId;
    }
    
    public void setSubId(final int id) {
        this.m_subId = id;
    }
    
    public int getSubId() {
        return this.m_subId;
    }
    
    @Override
    public byte[] encode() {
        return null;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        return true;
    }
    
    @Override
    public int getId() {
        return Integer.MIN_VALUE;
    }
    
    public long getTimeStamp() {
        return this.m_timeStamp;
    }
    
    public void setTimeStamp(final long timeStamp) {
        this.m_timeStamp = timeStamp;
    }
    
    @Override
    public String toString() {
        return "ClockMessage clockId=" + this.m_clockId + ", subClockId=" + this.m_subId + ", timestamp=" + this.m_timeStamp;
    }
}
