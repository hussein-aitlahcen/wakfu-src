package com.ankamagames.wakfu.common.game.nation.event;

public interface NationPoliticEventHandler extends NationEventHandler
{
    void onCitizenHasVoted(long p0, long p1);
    
    void onCandidateRegistered(long p0);
    
    void onCandidateWithdraw(long p0);
    
    void onCandidateRevalidate(long p0);
    
    void onForbiddenCandidatesChanged();
}
