package com.ankamagames.wakfu.common.game.fightChallenge;

public interface FightChallengeContextEventListener
{
    void onFightStart();
    
    void onChallengeAdded(int p0);
    
    void onChallengeRemoved(int p0);
    
    void onChallengeStateChanged(int p0, FightChallengeState p1);
}
