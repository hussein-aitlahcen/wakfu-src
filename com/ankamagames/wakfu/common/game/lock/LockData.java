package com.ankamagames.wakfu.common.game.lock;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class LockData implements LockInstance
{
    private final int m_lockId;
    private final int m_lockedItem;
    private final int m_lockValue;
    private final GameIntervalConst m_periodDuration;
    private final GameDateConst m_periodStartTime;
    private GameDateConst m_unlockDate;
    private GameDateConst m_lockDate;
    private GameDateConst m_currentLockValueDate;
    private int m_currentLockValue;
    private final boolean m_availableToCitizensOnly;
    
    public LockData(final LockDefinition lockDefinition) {
        super();
        this.m_lockDate = GameDate.getNullDate();
        this.m_currentLockValueDate = GameDate.getNullDate();
        this.m_lockId = lockDefinition.getId();
        this.m_lockedItem = lockDefinition.getLockedItem();
        this.m_lockValue = lockDefinition.getLockValue();
        this.m_periodDuration = lockDefinition.getPeriodDuration();
        this.m_periodStartTime = lockDefinition.getPeriodStartTime();
        this.m_unlockDate = lockDefinition.getUnlockDate();
        this.m_availableToCitizensOnly = lockDefinition.isAvailableToCitizensOnly();
    }
    
    @Override
    public GameDateConst getLockDate() {
        return this.m_lockDate;
    }
    
    public void setLockDate(final GameDateConst lockDate) {
        this.m_lockDate = lockDate;
    }
    
    public void setUnlockDate(final GameDateConst unlockDate) {
        this.m_unlockDate = unlockDate;
    }
    
    @Override
    public GameDateConst getCurrentLockValueLastChange() {
        return this.m_currentLockValueDate;
    }
    
    public void setCurrentLockValue(final int currentLockValue) {
        this.m_currentLockValue = currentLockValue;
    }
    
    public void setCurrentLockValueDate(final GameDateConst currentLockValueDate) {
        this.m_currentLockValueDate = currentLockValueDate;
    }
    
    public void incrementCurrentLockValue() {
        ++this.m_currentLockValue;
    }
    
    public void resetCurrentLockValue() {
        this.m_currentLockValue = 0;
    }
    
    @Override
    public int getCurrentLockValue() {
        return this.m_currentLockValue;
    }
    
    @Override
    public int getLockValue() {
        return this.m_lockValue;
    }
    
    @Override
    public int getId() {
        return this.m_lockId;
    }
    
    @Override
    public int getLockedItem() {
        return this.m_lockedItem;
    }
    
    @Override
    public GameDateConst getPeriodStartTime() {
        return this.m_periodStartTime;
    }
    
    @Override
    public GameIntervalConst getPeriodDuration() {
        return this.m_periodDuration;
    }
    
    @Override
    public GameDateConst getUnlockDate() {
        return this.m_unlockDate;
    }
    
    @Override
    public boolean isAvailableToCitizensOnly() {
        return this.m_availableToCitizensOnly;
    }
    
    @Override
    public String toString() {
        return "LockData{m_lockId=" + this.m_lockId + ", m_lockedItem=" + this.m_lockedItem + ", m_lockValue=" + this.m_lockValue + ", m_periodDuration=" + this.m_periodDuration + ", m_periodStartTime=" + this.m_periodStartTime + ", m_unlockDate=" + this.m_unlockDate + ", m_lockDate=" + this.m_lockDate + ", m_currentLockValueDate=" + this.m_currentLockValueDate + ", m_currentLockValue=" + this.m_currentLockValue + ", m_availableToCitizensOnly=" + this.m_availableToCitizensOnly + '}';
    }
}
