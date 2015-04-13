package com.ankamagames.wakfu.client.core.game.fight.join.team;

import com.ankamagames.wakfu.common.game.fight.*;

class UnjoinableFightJoinTeamValidator implements FightJoinTeamValidator
{
    private final JoinFightResult m_errorCode;
    
    UnjoinableFightJoinTeamValidator(final JoinFightResult errorCode) {
        super();
        this.m_errorCode = errorCode;
    }
    
    @Override
    public JoinFightResult canJoinTeam() {
        return this.m_errorCode;
    }
}
