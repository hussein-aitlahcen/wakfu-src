package com.ankamagames.framework.kernel.core.common.message.scheduler.clock;

import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.message.*;

class SchedulerListener implements TLinkable, Comparable
{
    protected TLinkable m_next;
    protected TLinkable m_previous;
    protected int m_subId;
    protected long m_clockId;
    protected long m_clockDelayMS;
    protected long m_nextTime;
    protected int m_repetitionsCount;
    protected int m_repetitionsIndex;
    private boolean m_bDiscarded;
    protected MessageHandler m_item;
    
    public SchedulerListener() {
        super();
        this.m_subId = 0;
        this.m_repetitionsIndex = 0;
        this.m_bDiscarded = false;
    }
    
    public void setSubId(final int subId) {
        this.m_subId = subId;
    }
    
    public int getSubId() {
        return this.m_subId;
    }
    
    @Override
    public TLinkable getNext() {
        return this.m_next;
    }
    
    @Override
    public void setNext(final TLinkable linkable) {
        this.m_next = linkable;
    }
    
    @Override
    public TLinkable getPrevious() {
        return this.m_previous;
    }
    
    @Override
    public void setPrevious(final TLinkable linkable) {
        this.m_previous = linkable;
    }
    
    public long getClockId() {
        return this.m_clockId;
    }
    
    void setClockId(final long clockId) {
        this.m_clockId = clockId;
    }
    
    public int getRepetitionsCount() {
        return this.m_repetitionsCount;
    }
    
    public void setRepetitionsCount(final int repetitionsCount) {
        this.m_repetitionsCount = repetitionsCount;
    }
    
    public void setClockDelay(final long delayMS) {
        this.m_clockDelayMS = delayMS;
    }
    
    public long getClockDelay() {
        return this.m_clockDelayMS;
    }
    
    public long getNextTime() {
        return this.m_nextTime;
    }
    
    public boolean canBeRepeated() {
        return this.m_repetitionsCount == -1 || this.m_repetitionsIndex <= this.m_repetitionsCount;
    }
    
    public MessageHandler getItem() {
        return this.m_item;
    }
    
    public void setItem(final MessageHandler item) {
        this.m_item = item;
    }
    
    void setTriggered(final long now) {
        ++this.m_repetitionsIndex;
        this.m_nextTime = now + this.m_clockDelayMS;
    }
    
    public boolean isDiscarded() {
        return this.m_bDiscarded;
    }
    
    public void discard() {
        this.m_bDiscarded = true;
    }
    
    @Override
    public int compareTo(final Object o) {
        final SchedulerListener listener = (SchedulerListener)o;
        if (listener == null) {
            throw new UnsupportedOperationException("Comparaison d'un listener avec null.");
        }
        if (this.m_nextTime < listener.m_nextTime) {
            return -1;
        }
        if (this.m_nextTime > listener.m_nextTime) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return this.m_item.getClass().getName() + ", nextTime : " + this.m_nextTime;
    }
}
