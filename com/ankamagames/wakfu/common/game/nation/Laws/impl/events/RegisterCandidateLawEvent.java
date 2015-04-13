package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class RegisterCandidateLawEvent extends NationLawEvent
{
    public RegisterCandidateLawEvent(final Citizen citizen) {
        super(citizen);
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.REGISTER_CANDIDATE;
    }
}
