package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

public final class AchievementHistoryEntry
{
    private final long m_unlockTime;
    private final int m_achievementId;
    
    AchievementHistoryEntry(final int achievementId, final long unlockTime) {
        super();
        this.m_unlockTime = unlockTime;
        this.m_achievementId = achievementId;
    }
    
    public long getUnlockTime() {
        return this.m_unlockTime;
    }
    
    public int getAchievementId() {
        return this.m_achievementId;
    }
}
