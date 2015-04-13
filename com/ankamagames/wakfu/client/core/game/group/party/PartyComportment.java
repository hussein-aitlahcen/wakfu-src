package com.ankamagames.wakfu.client.core.game.group.party;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.clientToServer.group.*;

public class PartyComportment
{
    private final LocalPlayerCharacter m_localPlayer;
    private PartyModel m_party;
    private ArrayList<PartyComportmentListener> m_listeners;
    
    public PartyComportment(final LocalPlayerCharacter localPlayer) {
        super();
        this.m_listeners = new ArrayList<PartyComportmentListener>();
        this.m_localPlayer = localPlayer;
    }
    
    public final PartyModel getParty() {
        return this.m_party;
    }
    
    public long getPartyId() {
        if (this.m_party != null) {
            return this.m_party.getId();
        }
        return 0L;
    }
    
    public boolean isPartyLeader() {
        return this.m_party != null && this.m_party.getLeaderId() == this.m_localPlayer.getId();
    }
    
    public boolean isInParty() {
        return this.m_party != null;
    }
    
    public void inviteSomeone(final String userName) {
        if (!ChatHelper.controlAction(new Action(userName, 2))) {
            return;
        }
        final String chatMessage = WakfuTranslator.getInstance().getString("group.party.inviting", userName);
        ChatManager.getInstance().pushMessage(chatMessage, 4);
        final GroupClientInvitationRequestMessage message = new GroupClientInvitationRequestMessage();
        message.setGroupType(GroupType.PARTY.getId());
        try {
            final long userId = Long.parseLong(userName);
            message.setRequestedPlayerId(userId);
        }
        catch (NumberFormatException e) {
            message.setRequestedPlayerName(userName);
        }
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
    }
    
    public void exclude(final long characterId) {
        if (this.m_party != null) {
            final GroupExcludeMemberMessage message = new GroupExcludeMemberMessage();
            message.setGroupId(this.m_party.getId());
            message.setCharacterId(characterId);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        }
    }
    
    public void setParty(final PartyModel party) {
        if (party == this.m_party) {
            return;
        }
        if (party != null) {
            this.firePartyJoin(party);
        }
        else {
            this.firePartyLeave(this.m_party);
        }
        this.m_party = party;
        WakfuGameEntity.getInstance().getLocalPlayer().updateAdditionalAppearance();
    }
    
    public void addPartyComportmentListener(final PartyComportmentListener listener) {
        if (this.m_listeners.contains(listener)) {
            return;
        }
        this.m_listeners.add(listener);
    }
    
    public void removePartyComportmentListener(final PartyComportmentListener listener) {
        if (!this.m_listeners.contains(listener)) {
            return;
        }
        this.m_listeners.remove(listener);
    }
    
    public void firePartyJoin(final PartyModel party) {
        for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
            this.m_listeners.get(i).onJoinParty(party);
        }
    }
    
    public void firePartyLeave(final PartyModel party) {
        for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
            this.m_listeners.get(i).onLeaveParty(party);
        }
    }
}
