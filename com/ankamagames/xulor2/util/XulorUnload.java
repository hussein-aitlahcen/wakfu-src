package com.ankamagames.xulor2.util;

public class XulorUnload implements XulorLoadUnload
{
    private String m_id;
    private boolean m_all;
    private long m_startTime;
    private int m_duration;
    private boolean m_ready;
    
    public XulorUnload(final String id, final int duration, final long startTime) {
        super();
        this.m_all = false;
        this.m_ready = true;
        this.m_id = id;
        this.m_duration = duration;
        this.m_startTime = startTime;
        this.m_ready = (this.m_duration == Integer.MAX_VALUE);
    }
    
    public XulorUnload(final String id) {
        this(id, Integer.MAX_VALUE, 0L);
    }
    
    public XulorUnload(final boolean all) {
        super();
        this.m_all = false;
        this.m_ready = true;
        this.m_all = all;
    }
    
    public String getId() {
        return this.m_id;
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public long getStartTime() {
        return this.m_startTime;
    }
    
    public void setStartTime(final long startTime) {
        this.m_startTime = startTime;
    }
    
    public boolean isAll() {
        return this.m_all;
    }
    
    public boolean isReady() {
        return this.m_ready;
    }
    
    public void setReady() {
        this.m_ready = true;
    }
}
