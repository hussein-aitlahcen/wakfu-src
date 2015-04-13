package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.*;

public interface ClientCitizenComportementObserver
{
    void nationSet(Nation p0);
    
    void hasVoted();
    
    void onJailCooldownUpdate(int p0);
    
    void onCitizenScoreChanged(int p0, int p1);
    
    void updateCandidateInfo();
    
    void updateAdditionalAppearance();
    
    void onPassportActiveChanged();
    
    void updatePvpTimer();
    
    void updatePvpState(boolean p0);
}
