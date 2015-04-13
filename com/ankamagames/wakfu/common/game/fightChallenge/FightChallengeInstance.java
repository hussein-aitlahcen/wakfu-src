package com.ankamagames.wakfu.common.game.fightChallenge;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class FightChallengeInstance implements FightChallenge
{
    private final FightChallenge m_model;
    private FightChallengeState m_state;
    private int m_dropLevel;
    private int m_xpLevel;
    
    public FightChallengeInstance(final FightChallenge model) {
        super();
        this.m_model = model;
        this.m_state = FightChallengeState.RUNNING;
    }
    
    public void setState(final FightChallengeState state) {
        this.m_state = state;
    }
    
    public FightChallengeState getState() {
        return this.m_state;
    }
    
    @Override
    public int getId() {
        return this.m_model.getId();
    }
    
    @Override
    public int getGfxId() {
        return this.m_model.getGfxId();
    }
    
    @Override
    public short getDropWeight() {
        return this.m_model.getDropWeight();
    }
    
    @Override
    public SimpleCriterion getCriterion() {
        return this.m_model.getCriterion();
    }
    
    @Override
    public int getStateId() {
        return this.m_model.getStateId();
    }
    
    @Override
    public int getListenedEffectSuccessId() {
        return this.m_model.getListenedEffectSuccessId();
    }
    
    @Override
    public int getListenedEffectFailureId() {
        return this.m_model.getListenedEffectFailureId();
    }
    
    @Override
    public ArrayList<FightChallengeReward> getRewards() {
        return this.m_model.getRewards();
    }
    
    @Override
    public boolean isChallengeIncompatible(final int challengeId) {
        return this.m_model.isChallengeIncompatible(challengeId);
    }
    
    @Override
    public boolean isMonsterIncompatible(final int monsterId) {
        return this.m_model.isMonsterIncompatible(monsterId);
    }
    
    @Override
    public boolean isBaseChallenge() {
        return this.m_model.isBaseChallenge();
    }
    
    public void setDropLevel(final int dropLevel) {
        this.m_dropLevel = dropLevel;
    }
    
    public void setXpLevel(final int xpLevel) {
        this.m_xpLevel = xpLevel;
    }
    
    public int getDropLevel() {
        return this.m_dropLevel;
    }
    
    public int getXpLevel() {
        return this.m_xpLevel;
    }
    
    public void dropRewards(final BasicCharacterInfo player, final AbstractFight fight) {
        this.m_xpLevel = 0;
        this.m_dropLevel = 0;
        final ArrayList<FightChallengeReward> rewards = this.m_model.getRewards();
        for (int i = 0, size = rewards.size(); i < size; ++i) {
            final FightChallengeReward reward = rewards.get(i);
            if (reward.getCriterion().isValid(player, player, fight, fight)) {
                this.m_xpLevel += reward.getXpLevel();
                this.m_dropLevel += reward.getDropLevel();
            }
        }
    }
    
    @Override
    public String toString() {
        return "FightChallengeInstance{m_model=" + this.m_model + ", m_state=" + this.m_state + ", m_dropLevel=" + this.m_dropLevel + ", m_xpLevel=" + this.m_xpLevel + '}';
    }
}
