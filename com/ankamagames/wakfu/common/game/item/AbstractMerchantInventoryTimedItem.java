package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.wakfu.common.game.time.calendar.*;

public abstract class AbstractMerchantInventoryTimedItem extends AbstractMerchantInventoryItem
{
    private long m_duration;
    private long m_startDate;
    public static final int INFINITE_DURATION = Integer.MAX_VALUE;
    
    public long getDuration() {
        return this.m_duration;
    }
    
    public void setDuration(final long duration) {
        this.m_duration = duration;
    }
    
    public long getStartDate() {
        return this.m_startDate;
    }
    
    public void setStartDate(final long startDate) {
        this.m_startDate = startDate;
    }
    
    public void setInfinite() {
        this.m_duration = 2147483647L;
    }
    
    public boolean isInfinite() {
        return this.m_duration == 2147483647L;
    }
    
    public boolean isActivated() {
        return this.m_startDate + this.m_duration > WakfuGameCalendar.getInstance().getInternalTimeInMs();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_duration = 0L;
        this.m_startDate = 0L;
    }
}
