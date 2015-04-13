package com.ankamagames.wakfu.client.core.game.protector;

public class ProtectorSecret
{
    private final int m_secretId;
    private final int m_achievementGoalId;
    private final int m_secretGfxId;
    private final int m_discoveredGfxId;
    
    public ProtectorSecret(final int secretId, final int achievementGoalId, final int secretGfxId, final int discoveredGfxId) {
        super();
        this.m_secretId = secretId;
        this.m_achievementGoalId = achievementGoalId;
        this.m_secretGfxId = secretGfxId;
        this.m_discoveredGfxId = discoveredGfxId;
    }
    
    public int getSecretId() {
        return this.m_secretId;
    }
    
    public int getAchievementGoalId() {
        return this.m_achievementGoalId;
    }
    
    public int getSecretGfxId() {
        return this.m_secretGfxId;
    }
    
    public int getDiscoveredGfxId() {
        return this.m_discoveredGfxId;
    }
}
