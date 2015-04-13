package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class VoteLawEvent extends NationLawEvent
{
    private final long m_candidateId;
    
    public VoteLawEvent(final Citizen citizen, final long candidateId) {
        super(citizen);
        this.m_candidateId = candidateId;
    }
    
    public long getCandidateId() {
        return this.m_candidateId;
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.VOTE;
    }
}
