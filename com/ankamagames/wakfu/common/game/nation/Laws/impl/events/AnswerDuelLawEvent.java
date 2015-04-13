package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class AnswerDuelLawEvent extends NationLawEvent
{
    private final boolean m_accepted;
    
    public AnswerDuelLawEvent(final Citizen citizen, final boolean accepted) {
        super(citizen);
        this.m_accepted = accepted;
    }
    
    public boolean isAccepted() {
        return this.m_accepted;
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.ANSWER_DUEL;
    }
}
