package com.ankamagames.wakfu.common.game.item.rent;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public final class FightLimitedRentInfo implements RentInfo
{
    private long m_durationInFights;
    private long m_fightCount;
    
    @Override
    public int getType() {
        return 1;
    }
    
    @Override
    public void setInitialDuration(final long rentDuration) {
        this.m_durationInFights = rentDuration;
    }
    
    @Override
    public boolean isExpired() {
        return this.m_fightCount >= this.m_durationInFights;
    }
    
    @Override
    public void toRaw(final RawRentInfo rawRentInfo) {
        rawRentInfo.type = this.getType();
        rawRentInfo.duration = this.m_durationInFights;
        rawRentInfo.count = this.m_fightCount;
    }
    
    @Override
    public void fromRaw(final RawRentInfo rawRentInfo) {
        this.m_durationInFights = rawRentInfo.duration;
        this.m_fightCount = rawRentInfo.count;
    }
    
    @Override
    public RentInfo getCopy() {
        final FightLimitedRentInfo res = new FightLimitedRentInfo();
        res.m_fightCount = this.m_fightCount;
        res.m_durationInFights = this.m_durationInFights;
        return res;
    }
    
    @Override
    public void addDuration(final long durationToAdd) {
        this.m_durationInFights += durationToAdd;
    }
    
    public void incFightCount() {
        ++this.m_fightCount;
    }
    
    @Override
    public String toString() {
        return "FightLimitedRentInfo{m_durationInFights=" + this.m_durationInFights + ", m_fightCount=" + this.m_fightCount + '}';
    }
    
    public long getRemainingFights() {
        return Math.max(0L, this.m_durationInFights - this.m_fightCount);
    }
}
