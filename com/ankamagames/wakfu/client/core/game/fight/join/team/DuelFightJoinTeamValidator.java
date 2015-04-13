package com.ankamagames.wakfu.client.core.game.fight.join.team;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

class DuelFightJoinTeamValidator extends FightJoinTeamValidatorBase
{
    DuelFightJoinTeamValidator(final FightInfo fight, final LocalPlayerCharacter player, final byte teamId) {
        super(fight, player, teamId);
    }
}
