package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

public final class Reward
{
    private final int m_id;
    private final Achievement m_achievement;
    private final int[] m_parameters;
    private final RewardType m_type;
    
    Reward(final int id, final Achievement achievement, final RewardType type, final int[] parameters) {
        super();
        this.m_id = id;
        this.m_achievement = achievement;
        this.m_parameters = parameters;
        this.m_type = type;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public Achievement getAchievement() {
        return this.m_achievement;
    }
    
    public int[] getParameters() {
        return this.m_parameters;
    }
    
    public RewardType getType() {
        return this.m_type;
    }
}
