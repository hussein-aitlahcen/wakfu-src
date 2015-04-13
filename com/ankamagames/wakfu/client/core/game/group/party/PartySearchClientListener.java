package com.ankamagames.wakfu.client.core.game.group.party;

import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import com.ankamagames.wakfu.client.ui.protocol.message.partySearch.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;

public final class PartySearchClientListener implements PartyModelListener
{
    @Override
    public void onMemberAdded(final PartyModelInterface party, final PartyMemberInterface member) {
        if (party.isFull()) {
            UIPartySearchFrame.cleanPartyRequester();
            return;
        }
        PartySearchFloodController.getInstance().removeEntry(member.getCharacterId());
        if (!UIPartySearchFrame.isPartyRequesterExists()) {
            final PartyPlayerDefinition definition = new PartyPlayerDefinition(member.getCharacterId(), PartySearchConstants.DEFAULT_PARTY_ROLE, member.getBreedId(), member.getLevel());
            definition.setName(member.getName());
            final Message message = new UIPartyPlayerDefinitionMessage(definition);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    @Override
    public void onMemberRemoved(final PartyModelInterface party, final PartyMemberInterface member) {
        if (party.getLeaderId() == member.getCharacterId()) {
            return;
        }
        if (member.getCharacterId() == WakfuGameEntity.getInstance().getLocalPlayer().getId()) {
            UIPartySearchFrame.cleanPartyRequester();
        }
        else {
            UIPartySearchFrame.removePartyPlayer(member.getCharacterId());
        }
    }
    
    @Override
    public void onLeaderChange(final PartyModelInterface party, final long previousLeader, final long newLeader) {
    }
}
