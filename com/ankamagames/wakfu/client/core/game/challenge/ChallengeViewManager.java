package com.ankamagames.wakfu.client.core.game.challenge;

import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class ChallengeViewManager
{
    public static final ChallengeViewManager INSTANCE;
    private final TIntObjectHashMap<AbstractChallengeView> m_challenges;
    private final TLongObjectHashMap<ChallengeGoalView> m_goals;
    private final TIntObjectHashMap<ChallengeReward> m_rewards;
    
    private ChallengeViewManager() {
        super();
        this.m_challenges = new TIntObjectHashMap<AbstractChallengeView>();
        this.m_goals = new TLongObjectHashMap<ChallengeGoalView>();
        this.m_rewards = new TIntObjectHashMap<ChallengeReward>();
    }
    
    public ChallengeReward getChallengeReward(final int rewardId, final ChallengeRewardModel model) {
        final ChallengeReward reward = this.m_rewards.get(rewardId);
        if (reward != null) {
            return reward;
        }
        final ChallengeReward newReward = new ChallengeReward(model);
        this.m_rewards.put(newReward.getId(), newReward);
        return newReward;
    }
    
    public AbstractChallengeView getChallengeView(final int challengeId) {
        AbstractChallengeView view = this.m_challenges.get(challengeId);
        if (view != null) {
            return view;
        }
        view = new ChallengeView(challengeId);
        this.m_challenges.put(challengeId, view);
        return view;
    }
    
    public ChallengeGoalView getChallengeGoalView(final int challengeId, final int goalId) {
        final long id = MathHelper.getLongFromTwoInt(challengeId, goalId);
        ChallengeGoalView view = this.m_goals.get(id);
        if (view != null) {
            return view;
        }
        view = new ChallengeGoalView(challengeId, goalId);
        this.m_goals.put(id, view);
        return view;
    }
    
    public void removeChallenge(final int challengeId) {
        this.m_challenges.remove(challengeId);
    }
    
    public void removeGoal(final int goalId) {
        this.m_goals.remove(goalId);
    }
    
    static {
        INSTANCE = new ChallengeViewManager();
    }
}
