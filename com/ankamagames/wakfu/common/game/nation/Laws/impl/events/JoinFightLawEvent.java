package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class JoinFightLawEvent extends NationLawEvent
{
    private final FightModel m_fightModel;
    private final BasicCharacterInfo m_attacker;
    private final BasicCharacterInfo m_defender;
    private final Collection<? extends BasicCharacterInfo> m_teammates;
    
    public JoinFightLawEvent(final Citizen citizen, final FightModel model, final BasicCharacterInfo attacker, final BasicCharacterInfo defender, final Collection<? extends BasicCharacterInfo> teammates) {
        super(citizen);
        this.m_fightModel = model;
        this.m_attacker = attacker;
        this.m_defender = defender;
        this.m_teammates = teammates;
    }
    
    public FightModel getFightModel() {
        return this.m_fightModel;
    }
    
    public BasicCharacterInfo getAttacker() {
        return this.m_attacker;
    }
    
    public BasicCharacterInfo getDefender() {
        return this.m_defender;
    }
    
    public Collection<? extends BasicCharacterInfo> getTeammates() {
        return this.m_teammates;
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.JOIN_FIGHT;
    }
}
