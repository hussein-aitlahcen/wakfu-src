package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class WonFightLawEvent extends NationLawEvent
{
    private final FightModel m_fightModel;
    
    public WonFightLawEvent(final Citizen citizen, final FightModel fightModel) {
        super(citizen);
        this.m_fightModel = fightModel;
    }
    
    public FightModel getFightModel() {
        return this.m_fightModel;
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.WON_FIGHT;
    }
}
