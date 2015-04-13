package com.ankamagames.wakfu.client.core.game.achievements;

import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.common.game.fightChallenge.*;

public class FightChallengeStaticView extends AbstractFightChallengeView
{
    private FightChallengeState m_state;
    private String m_rewards;
    
    public FightChallengeStaticView(final int challengeId, final FightChallengeState state, final String rewards) {
        super(challengeId);
        this.m_state = state;
        this.m_rewards = rewards;
    }
    
    @Override
    protected boolean isFailed() {
        return this.m_state == FightChallengeState.FAILURE;
    }
    
    @Override
    protected boolean isCompleted() {
        return this.m_state == FightChallengeState.SUCCESS;
    }
    
    @Override
    public String getRewards() {
        return this.m_rewards;
    }
}
