package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class ExchangeLawEvent extends NationLawEvent
{
    private final Citizen m_requestedCitizen;
    
    public ExchangeLawEvent(final Citizen citizen, final Citizen requestedCitizen) {
        super(citizen);
        this.m_requestedCitizen = requestedCitizen;
    }
    
    public Citizen getRequestedCitizen() {
        return this.m_requestedCitizen;
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.PROPOSE_EXCHANGE;
    }
}
