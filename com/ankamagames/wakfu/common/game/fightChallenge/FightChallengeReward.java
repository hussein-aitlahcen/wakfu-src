package com.ankamagames.wakfu.common.game.fightChallenge;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class FightChallengeReward
{
    private final int m_id;
    private final SimpleCriterion m_criterion;
    private final int m_xpLevel;
    private final int m_dropLevel;
    
    public FightChallengeReward(final int id, final SimpleCriterion criterion, final int xpLevel, final short dropLevel) {
        super();
        this.m_id = id;
        this.m_criterion = criterion;
        this.m_xpLevel = xpLevel;
        this.m_dropLevel = dropLevel;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    public int getXpLevel() {
        return this.m_xpLevel;
    }
    
    public int getDropLevel() {
        return this.m_dropLevel;
    }
    
    @Override
    public String toString() {
        return "FightChallengeReward{m_id=" + this.m_id + ", m_criterion=" + this.m_criterion + ", m_xpLevel=" + this.m_xpLevel + ", m_dropLevel=" + this.m_dropLevel + '}';
    }
}
