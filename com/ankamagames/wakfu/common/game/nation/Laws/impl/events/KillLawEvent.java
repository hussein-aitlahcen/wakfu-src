package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class KillLawEvent extends NationLawEvent
{
    private final BasicCharacterInfo m_killed;
    private final byte m_citizenTeamId;
    private final byte m_killedTeamId;
    private final FightModel m_fightModel;
    
    public KillLawEvent(final Citizen citizen, final BasicCharacterInfo killed, final byte citizenTeamId, final byte killedTeamId, final FightModel model) {
        super(citizen);
        this.m_killed = killed;
        this.m_citizenTeamId = citizenTeamId;
        this.m_killedTeamId = killedTeamId;
        this.m_fightModel = model;
    }
    
    public BasicCharacterInfo getKilled() {
        return this.m_killed;
    }
    
    public byte getCitizenTeamId() {
        return this.m_citizenTeamId;
    }
    
    public byte getKilledTeamId() {
        return this.m_killedTeamId;
    }
    
    public FightModel getFightModel() {
        return this.m_fightModel;
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.KILL;
    }
}
