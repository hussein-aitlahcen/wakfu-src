package com.ankamagames.wakfu.client.core.dungeon.loader;

import java.util.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.dungeon.ranks.*;
import com.ankamagames.wakfu.common.game.dungeon.rewards.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.binaryStorage.*;

public class DungeonDefinition implements RoundProvider
{
    private final int m_id;
    private final ArrayList<ChallengeDefinition> m_challenges;
    private int m_roundScoreBase;
    private int m_roundScoreIncr;
    private final RankScore[] m_rankScores;
    private final RewardList[] m_rewards;
    
    DungeonDefinition(final int id) {
        super();
        this.m_challenges = new ArrayList<ChallengeDefinition>();
        this.m_rankScores = new RankScore[20];
        this.m_rewards = new RewardList[20];
        this.m_id = id;
    }
    
    void addChallenge(final ChallengeDefinition challenge) {
        this.m_challenges.add(challenge);
    }
    
    public void foreachChallenge(final TObjectProcedure<ChallengeDefinition> procedure) {
        for (int i = 0, size = this.m_challenges.size(); i < size; ++i) {
            if (!procedure.execute(this.m_challenges.get(i))) {
                return;
            }
        }
    }
    
    @Override
    public int getRoundScore(final int roundIndex) {
        return this.m_roundScoreBase + roundIndex * this.m_roundScoreIncr;
    }
    
    public Rank getRank(final int playerLevel, final ScoreProvider score) {
        final int level = RewardList.getLevelIndex(playerLevel);
        return this.m_rankScores[level].getRank(score);
    }
    
    public boolean forEachReward(final TObjectProcedure<Reward> procedure, final int playerLevel) {
        final int level = RewardList.getLevelIndex(playerLevel);
        return this.m_rewards[level].forEachReward(procedure);
    }
    
    public boolean forEachValidReward(final TObjectProcedure<Reward> procedure, final int playerLevel, final ScoreProvider scoreProvider) {
        final int level = RewardList.getLevelIndex(playerLevel);
        final Rank rank = this.getRank(playerLevel, scoreProvider);
        return this.m_rewards[level].forEachValidReward(procedure, scoreProvider.getTotalScore(), rank);
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(117, this.m_id, new Object[0]);
    }
    
    public String getDescription() {
        return WakfuTranslator.getInstance().getString(116, this.m_id, new Object[0]);
    }
    
    void addRank(final ArcadeDungeonBinaryData.RankDef rank, final boolean skipChallengeRatio) {
        assert this.m_rankScores[rank.getLevelOrder()] == null;
        this.m_rankScores[rank.getLevelOrder()] = new RankScore(rank.getScoreMinA(), rank.getScoreMinB(), rank.getScoreMinC(), rank.getScoreMinD(), skipChallengeRatio);
    }
    
    void addRewards(final ArcadeDungeonBinaryData.RewardList rewardList) {
        assert this.m_rewards[rewardList.getLevelOrder()] == null;
        final RewardList list = new RewardList();
        for (final ArcadeDungeonBinaryData.Reward r : rewardList.getRewards()) {
            list.addReward(Reward.createReward(r.getItemId(), r.getXpValue(), r.getScoreMin(), r.getRankNeeded()));
        }
        this.m_rewards[rewardList.getLevelOrder()] = list;
    }
    
    void setRoundScore(final int scoreRoundBase, final int scoreRoundIncr) {
        this.m_roundScoreBase = scoreRoundBase;
        this.m_roundScoreIncr = scoreRoundIncr;
    }
    
    @Override
    public String toString() {
        return "DungeonDefinition{m_id=" + this.m_id + '}';
    }
}
