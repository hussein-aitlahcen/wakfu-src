package com.ankamagames.wakfu.client.core.game.fight.actionsOperations;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.game.fight.join.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

public final class ExternalFightPlacementStartOperations
{
    final FightPlacementStartMessage m_msg;
    
    public ExternalFightPlacementStartOperations(final FightPlacementStartMessage msg) {
        super();
        this.m_msg = msg;
    }
    
    public void execute() {
        final FightInfo fightInfo = FightManager.getInstance().getFightById(this.m_msg.getFightId());
        if (!(fightInfo instanceof ExternalFightInfo)) {
            return;
        }
        final ExternalFightInfo fight = (ExternalFightInfo)fightInfo;
        final WakfuGameEntity wakfuGameEntity = WakfuGameEntity.getInstance();
        if (wakfuGameEntity.hasFrame(UISpellsRestatFrame.INSTANCE)) {
            return;
        }
        final LocalPlayerCharacter localPlayer = wakfuGameEntity.getLocalPlayer();
        if (!WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer)) {
            return;
        }
        if (localPlayer.hasProperty(WorldPropertyType.JOIN_IN_FIGHT_MRU_DISABLED)) {
            return;
        }
        final PartyComportment partyComportment = localPlayer.getPartyComportment();
        if (!partyComportment.isInParty()) {
            return;
        }
        final PartyModelInterface party = partyComportment.getParty();
        for (final CharacterInfo fighter : fight.getFighters()) {
            if (party.contains(fighter.getId()) && this.autoJoinGroupMemberFight(fight, fighter)) {
                break;
            }
        }
    }
    
    private boolean autoJoinGroupMemberFight(final ExternalFightInfo fight, final CharacterInfo fighter) {
        final JoinFightProcedure joinFightProc = JoinFight.joinGroupMember(fight, fighter);
        if (joinFightProc.canJoinFight() != JoinFightResult.OK) {
            this.notifyFightStartToChat(fighter);
            return false;
        }
        if (joinFightProc.isJoinProtectorAttack()) {
            this.sendStakeRequest(fight);
            return true;
        }
        final byte typeId = fight.getModel().getTypeId();
        String inviteToJoin = WakfuTranslator.getInstance().getString("party.question.joinPartyMemberInFight", String.format("<b>%s</b>", fighter.getName()), typeId);
        MRUJoinFightAction mruJoinFightAction = new MRUJoinFightAction();
        mruJoinFightAction.initFromSource(fighter);
        final String addedText = mruJoinFightAction.getComplementaryTooltip();
        if (addedText != null) {
            inviteToJoin = inviteToJoin + "\n" + addedText;
        }
        mruJoinFightAction = null;
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(inviteToJoin, WakfuMessageBoxConstants.getMessageBoxIconUrl(5), 2073L, 102, 1);
        fight.setCurrentNotification(messageBoxControler);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                fight.notificationExpired(messageBoxControler);
                if ((type & 0x8) != 0x8) {
                    return;
                }
                final JoinFightResult joinStatus = joinFightProc.tryJoinFight();
                if (joinStatus == JoinFightResult.OK) {
                    return;
                }
                ErrorsMessageTranslator.getInstance().pushMessage(joinStatus.getErrorCode(), 3, fighter);
            }
        });
        return true;
    }
    
    private void sendStakeRequest(final ExternalFightInfo fight) {
        final Collection<CharacterInfo> fighters = fight.getFighters();
        for (final CharacterInfo info : fighters) {
            if (info.getType() == 1) {
                if (!(info instanceof NonPlayerCharacter)) {
                    continue;
                }
                final NonPlayerCharacter npc = (NonPlayerCharacter)info;
                final Protector protector = npc.getProtector();
                if (protector == null) {
                    continue;
                }
                final Message getStakeMessage = new GetProtectorFightStakeRequestMessage(protector.getId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(getStakeMessage);
            }
        }
    }
    
    private void notifyFightStartToChat(final CharacterInfo fighter) {
        final String notifyStart = WakfuTranslator.getInstance().getString("party.notice.partyMemberJoinFight", fighter.getName());
        final ChatMessage chatMessage = new ChatMessage(notifyStart);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
}
