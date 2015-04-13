package com.ankamagames.wakfu.common.game.fightChallenge;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import org.jetbrains.annotations.*;
import java.util.*;

public class FightChallengeContext
{
    protected final IntObjectLightWeightMap<FightChallengeInstance> m_challenges;
    private FightChallengeContextEventListener m_listener;
    
    public FightChallengeContext() {
        super();
        this.m_challenges = new IntObjectLightWeightMap<FightChallengeInstance>();
    }
    
    public void setListener(final FightChallengeContextEventListener listener) {
        this.m_listener = listener;
    }
    
    public void addChallenge(final FightChallenge challenge) {
        this.addChallenge(challenge, 0, 0);
    }
    
    public void addChallenge(final FightChallenge challenge, final int dropLevel, final int xpLevel) {
        final FightChallengeInstance instance = new FightChallengeInstance(challenge);
        this.m_challenges.put(challenge.getId(), instance);
        instance.setDropLevel(dropLevel);
        instance.setXpLevel(xpLevel);
        if (this.m_listener != null) {
            this.m_listener.onChallengeAdded(challenge.getId());
        }
    }
    
    protected FightChallengeInstance getChallenge(final int id) {
        return this.m_challenges.get(id);
    }
    
    public void setChallengeState(final int challengeId, @NotNull final FightChallengeState state) {
        final FightChallengeInstance challengeInstance = this.m_challenges.get(challengeId);
        if (challengeInstance != null) {
            challengeInstance.setState(state);
            if (this.m_listener != null) {
                this.m_listener.onChallengeStateChanged(challengeId, state);
            }
        }
    }
    
    @Nullable
    public FightChallengeState getChallengeState(final int challengeId) {
        final FightChallengeInstance instance = this.m_challenges.get(challengeId);
        return (instance != null) ? instance.getState() : null;
    }
    
    public int getChallengeDropLevel(final int challengeId) {
        final FightChallengeInstance instance = this.m_challenges.get(challengeId);
        return (instance != null) ? instance.getDropLevel() : 0;
    }
    
    public int getChallengeXpLevel(final int challengeId) {
        final FightChallengeInstance instance = this.m_challenges.get(challengeId);
        return (instance != null) ? instance.getXpLevel() : 0;
    }
    
    public void init() {
        if (this.m_listener != null) {
            this.m_listener.onFightStart();
        }
    }
    
    public void clear() {
        if (this.m_listener != null) {
            for (final FightChallengeInstance challenge : this.m_challenges) {
                this.m_listener.onChallengeRemoved(challenge.getId());
            }
        }
        this.m_challenges.clear();
    }
}
