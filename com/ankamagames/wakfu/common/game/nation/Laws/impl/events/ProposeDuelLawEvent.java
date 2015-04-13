package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class ProposeDuelLawEvent extends NationLawEvent
{
    public ProposeDuelLawEvent(final Citizen citizen) {
        super(citizen);
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.PROPOSE_DUEL;
    }
}
