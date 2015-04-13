package com.ankamagames.wakfu.common.game.item.rent;

import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public final class TimeLimitedRentInfo implements RentInfo
{
    private long m_rentDuration;
    private GameDate m_buyDate;
    private GameInterval m_gameInterval;
    
    @Override
    public int getType() {
        return 2;
    }
    
    @Override
    public void setInitialDuration(final long rentDuration) {
        this.m_rentDuration = rentDuration * 1000L;
        this.m_gameInterval = GameInterval.fromLong(this.m_rentDuration);
    }
    
    @Override
    public boolean isExpired() {
        final GameDate date = WakfuGameCalendar.getInstance().getNewDate();
        date.sub(this.m_gameInterval);
        return date.after(this.m_buyDate);
    }
    
    @Override
    public void toRaw(final RawRentInfo rawRentInfo) {
        rawRentInfo.type = this.getType();
        rawRentInfo.duration = this.m_rentDuration;
        rawRentInfo.count = this.m_buyDate.toLong();
    }
    
    @Override
    public void fromRaw(final RawRentInfo rawRentInfo) {
        this.m_rentDuration = rawRentInfo.duration;
        this.m_gameInterval = GameInterval.fromLong(this.m_rentDuration);
        this.m_buyDate = GameDate.fromLong(rawRentInfo.count);
    }
    
    @Override
    public RentInfo getCopy() {
        final TimeLimitedRentInfo res = new TimeLimitedRentInfo();
        res.m_buyDate = this.m_buyDate;
        res.m_rentDuration = this.m_rentDuration;
        res.m_gameInterval = new GameInterval(this.m_gameInterval);
        return res;
    }
    
    @Override
    public void addDuration(final long durationToAddInSecond) {
        this.m_rentDuration += durationToAddInSecond * 1000L;
    }
    
    public void setBuyDate(final GameDate buyDate) {
        this.m_buyDate = buyDate;
    }
    
    @Override
    public String toString() {
        return "TimeLimitedRentInfo{m_rentDuration=" + this.m_rentDuration + ", m_buyDate=" + this.m_buyDate + '}';
    }
    
    public GameInterval getRemainingTime() {
        final GameDate date = WakfuGameCalendar.getInstance().getNewDate();
        final GameDate endDate = new GameDate(this.m_buyDate).add(this.m_gameInterval);
        return date.timeTo(endDate);
    }
}
