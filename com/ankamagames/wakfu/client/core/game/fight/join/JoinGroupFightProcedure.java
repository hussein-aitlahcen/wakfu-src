package com.ankamagames.wakfu.client.core.game.fight.join;

import com.ankamagames.wakfu.client.core.game.fight.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;

class JoinGroupFightProcedure extends JoinFightProcedureBase
{
    JoinGroupFightProcedure(@NotNull final FightInfo fight, @NotNull final LocalPlayerCharacter player, @NotNull final CharacterInfo joinedGroupMember) {
        super(fight, player);
        this.setJoinedAlly(joinedGroupMember);
    }
    
    @Override
    JoinFightResult tryJoinFightCore() {
        final PartyComportment partyData = this.m_player.getPartyComportment();
        if (!partyData.isInParty() || !partyData.getParty().contains(this.m_joinedAlly.getId())) {
            return JoinFightResult.TARGET_NOT_IN_PARTY;
        }
        return this.joinAlly();
    }
}
