package com.ankamagames.wakfu.client.core.game.fight.join.team;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fight.*;

abstract class FightJoinTeamValidatorBase implements FightJoinTeamValidator
{
    final FightInfo m_fight;
    final LocalPlayerCharacter m_player;
    final byte m_teamId;
    
    FightJoinTeamValidatorBase(final FightInfo fight, final LocalPlayerCharacter player, final byte teamId) {
        super();
        this.m_fight = fight;
        this.m_player = player;
        this.m_teamId = teamId;
    }
    
    @Override
    public JoinFightResult canJoinTeam() {
        return ((BasicFightInfo<LocalPlayerCharacter>)this.m_fight).canJoinTeam(this.m_player, this.m_teamId);
    }
}
