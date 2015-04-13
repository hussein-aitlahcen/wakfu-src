package com.ankamagames.wakfu.client.core.game.challenge;

import gnu.trove.*;

public interface ChallengeEventListener
{
    void loadActions(TIntArrayList p0, int p1);
    
    boolean updateVars(byte p0, long p1, int p2);
    
    void onChallengeChanged(ChallengeData p0);
    
    void onChallengeFinished(ChallengeData p0);
}
