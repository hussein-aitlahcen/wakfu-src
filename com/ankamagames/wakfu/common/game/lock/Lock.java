package com.ankamagames.wakfu.common.game.lock;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class Lock implements LockDefinition
{
    private final int m_id;
    private final int m_lockedItem;
    private final int m_lockValue;
    private final GameIntervalConst m_periodDuration;
    private final GameDateConst m_periodStartTime;
    private GameDateConst m_unlockDate;
    private final boolean m_isAvailableToCitizens;
    
    public Lock(final int id, final int lockedItem, final int lockValue, final GameDateConst periodStartTime, final GameIntervalConst periodDuration, final GameDateConst unlockDate, final boolean availableToCitizens) {
        super();
        this.m_id = id;
        this.m_lockedItem = lockedItem;
        this.m_periodDuration = periodDuration;
        this.m_periodStartTime = periodStartTime;
        this.m_unlockDate = unlockDate;
        this.m_isAvailableToCitizens = availableToCitizens;
        this.m_lockValue = (periodDuration.isEmpty() ? 0 : lockValue);
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public int getLockedItem() {
        return this.m_lockedItem;
    }
    
    @Override
    public int getLockValue() {
        return this.m_lockValue;
    }
    
    @Override
    public GameIntervalConst getPeriodDuration() {
        return this.m_periodDuration;
    }
    
    @Override
    public GameDateConst getPeriodStartTime() {
        return this.m_periodStartTime;
    }
    
    @Override
    public GameDateConst getUnlockDate() {
        return this.m_unlockDate;
    }
    
    @Override
    public boolean isAvailableToCitizensOnly() {
        return this.m_isAvailableToCitizens;
    }
}
