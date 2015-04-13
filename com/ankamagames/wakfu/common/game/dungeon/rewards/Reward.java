package com.ankamagames.wakfu.common.game.dungeon.rewards;

import com.ankamagames.wakfu.common.game.dungeon.ranks.*;

public class Reward
{
    private final Type m_type;
    private final int m_value;
    private final int m_scoreMin;
    private final Rank m_rankNeeded;
    
    Reward(final Type type, final int value, final int scoreMin, final Rank rankNeeded) {
        super();
        this.m_type = type;
        this.m_value = value;
        this.m_scoreMin = scoreMin;
        this.m_rankNeeded = rankNeeded;
    }
    
    public static Reward createReward(final int itemId, final int xpValue, final int scoreMin, final byte rankNeeded) {
        final Rank rank = Rank.getRank(rankNeeded);
        if (itemId == 0) {
            return new Reward(Type.Xp, xpValue, scoreMin, rank);
        }
        return new Reward(Type.Item, itemId, scoreMin, rank);
    }
    
    public Type getType() {
        return this.m_type;
    }
    
    public int getValue() {
        return this.m_value;
    }
    
    int getScoreMin() {
        return this.m_scoreMin;
    }
    
    public boolean isValidFor(final int score, final Rank rank) {
        return score >= this.m_scoreMin && rank.isBetterThan(this.m_rankNeeded);
    }
    
    public enum Type
    {
        Item, 
        Xp;
    }
}
