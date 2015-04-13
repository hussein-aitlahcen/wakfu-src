package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class LoseFightLawEvent extends NationLawEvent
{
    private final FightModel m_fightModel;
    private final BasicCharacterInfo m_initiatingFighter;
    
    public LoseFightLawEvent(final Citizen citizen, final FightModel fightModel, final BasicCharacterInfo initiatingFighter) {
        super(citizen);
        this.m_fightModel = fightModel;
        this.m_initiatingFighter = initiatingFighter;
    }
    
    public FightModel getFightModel() {
        return this.m_fightModel;
    }
    
    public BasicCharacterInfo getInitiatingFighter() {
        return this.m_initiatingFighter;
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.LOSE_FIGHT;
    }
}
