package com.ankamagames.wakfu.common.game.dungeon.rewards;

import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.dungeon.ranks.*;

public class RewardList
{
    private static final int LEVEL_MAX = 200;
    private static final int LEVEL_DELTA = 10;
    public static final int LEVEL_COUNT = 20;
    private static final Comparator<Reward> COMPARATOR;
    private final SortedList<Reward> m_rewards;
    
    public RewardList() {
        super();
        this.m_rewards = new SortedList<Reward>(4, RewardList.COMPARATOR);
    }
    
    public void addReward(final Reward r) {
        this.m_rewards.add(r);
    }
    
    public boolean forEachReward(final TObjectProcedure<Reward> procedure) {
        for (int i = 0, size = this.m_rewards.size(); i < size; ++i) {
            final Reward reward = this.m_rewards.get(i);
            if (!procedure.execute(reward)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachValidReward(final TObjectProcedure<Reward> procedure, final int score, final Rank rank) {
        for (int i = 0, size = this.m_rewards.size(); i < size; ++i) {
            final Reward reward = this.m_rewards.get(i);
            if (!reward.isValidFor(score, rank)) {
                return false;
            }
            if (!procedure.execute(reward)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "RewardList{m_rewards=" + this.m_rewards.size() + '}';
    }
    
    public static int getLevelIndex(final int playerLevel) {
        assert playerLevel > 0 && playerLevel <= 200;
        return (playerLevel - 1) / 10;
    }
    
    public static int getMinLevel(final int order) {
        assert order >= 0 && order < 20;
        return order * 10 + 1;
    }
    
    public static int getMaxLevel(final int order) {
        assert order >= 0 && order < 20;
        return (order + 1) * 10;
    }
    
    static {
        COMPARATOR = new Comparator<Reward>() {
            @Override
            public int compare(final Reward o1, final Reward o2) {
                return o1.getScoreMin() - o2.getScoreMin();
            }
        };
    }
}
