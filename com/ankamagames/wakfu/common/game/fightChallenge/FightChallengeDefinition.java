package com.ankamagames.wakfu.common.game.fightChallenge;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import gnu.trove.*;
import java.util.*;

public class FightChallengeDefinition implements FightChallenge
{
    private final int m_id;
    private final short m_dropWeight;
    private final SimpleCriterion m_dropCriterion;
    private final int m_stateId;
    private final int m_listenedEffectSuccessId;
    private final int m_listenedEffectFailureId;
    private final int m_gfxId;
    private final boolean m_isBase;
    private final TIntHashSet m_incompatibleChallenges;
    private final TIntHashSet m_incompatibleMonsters;
    private final ArrayList<FightChallengeReward> m_rewards;
    
    public FightChallengeDefinition(final int id, final short dropWeight, final SimpleCriterion dropCriterion, final int stateId, final int listenedEffectSuccessId, final int listenedEffectFailureId, final int gfxId, final boolean isBase) {
        super();
        this.m_incompatibleChallenges = new TIntHashSet();
        this.m_incompatibleMonsters = new TIntHashSet();
        this.m_rewards = new ArrayList<FightChallengeReward>();
        this.m_id = id;
        this.m_dropWeight = dropWeight;
        this.m_dropCriterion = dropCriterion;
        this.m_stateId = stateId;
        this.m_listenedEffectSuccessId = listenedEffectSuccessId;
        this.m_listenedEffectFailureId = listenedEffectFailureId;
        this.m_gfxId = gfxId;
        this.m_isBase = isBase;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    @Override
    public short getDropWeight() {
        return this.m_dropWeight;
    }
    
    @Override
    public SimpleCriterion getCriterion() {
        return this.m_dropCriterion;
    }
    
    @Override
    public int getStateId() {
        return this.m_stateId;
    }
    
    @Override
    public int getListenedEffectSuccessId() {
        return this.m_listenedEffectSuccessId;
    }
    
    @Override
    public int getListenedEffectFailureId() {
        return this.m_listenedEffectFailureId;
    }
    
    public void addAllIncompatibleChallenges(final int... incompatibleChallenges) {
        this.m_incompatibleChallenges.addAll(incompatibleChallenges);
    }
    
    public void addAllIncompatibleMonsters(final int... incompatibleMonsters) {
        this.m_incompatibleMonsters.addAll(incompatibleMonsters);
    }
    
    public void addReward(final int rewardId, final SimpleCriterion criterion, final short level, final short dropLevel) {
        final FightChallengeReward reward = new FightChallengeReward(rewardId, criterion, level, dropLevel);
        this.m_rewards.add(reward);
    }
    
    @Override
    public ArrayList<FightChallengeReward> getRewards() {
        return this.m_rewards;
    }
    
    @Override
    public boolean isChallengeIncompatible(final int challengeId) {
        return this.m_incompatibleChallenges.contains(challengeId);
    }
    
    @Override
    public boolean isMonsterIncompatible(final int monsterId) {
        return this.m_incompatibleMonsters.contains(monsterId);
    }
    
    @Override
    public boolean isBaseChallenge() {
        return this.m_isBase;
    }
}
