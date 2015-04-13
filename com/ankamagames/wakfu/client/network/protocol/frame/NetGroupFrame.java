package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.client.core.game.group.party.member.*;
import com.ankamagames.wakfu.common.game.group.member.serialization.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.group.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.clientToServer.group.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.serverToClient.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import java.util.*;
import gnu.trove.*;

public class NetGroupFrame implements MessageFrame
{
    private static final boolean DEBUG_MODE = false;
    private static final Logger m_logger;
    private static final NetGroupFrame m_instance;
    
    public static NetGroupFrame getInstance() {
        return NetGroupFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 500: {
                final GroupGlobalDataUpdateMessage msg = (GroupGlobalDataUpdateMessage)message;
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final PartyComportment partyComp = localPlayer.getPartyComportment();
                final GroupType groupType = GroupType.getFromGroupId(msg.getGroupId());
                switch (groupType) {
                    case PARTY: {
                        final boolean wasInParty = partyComp.isInParty();
                        TLongArrayList membersId = null;
                        if (wasInParty) {
                            membersId = new TLongArrayList(partyComp.getParty().getMemberCount());
                            final TLongObjectIterator<PartyMemberInterface> it = partyComp.getParty().getMembers().iterator();
                            while (it.hasNext()) {
                                it.advance();
                                membersId.add(it.value().getCharacterId());
                            }
                        }
                        boolean needUIUpdate = false;
                        if (!wasInParty || partyComp.getPartyId() != msg.getGroupId()) {
                            final PartyModel party = new PartyModel(msg.getGroupId());
                            party.addListener(new MemberListModificationListener());
                            party.addListener(new PartySearchClientListener());
                            if (WakfuGameEntity.getInstance().hasFrame(UICompanionsManagementFrame.INSTANCE)) {
                                party.addListener(UICompanionsManagementFrame.INSTANCE);
                            }
                            partyComp.setParty(party);
                            needUIUpdate = true;
                            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventJointGroup());
                        }
                        final byte[] groupData = msg.getGroupData();
                        if (groupData != null) {
                            final PartySerializer serializer = new PartySerializer(partyComp.getParty());
                            serializer.unserializeGroupData(groupData);
                            needUIUpdate = true;
                        }
                        for (final ObjectPair<Long, byte[]> entry : msg.getSerializedCharacterData()) {
                            final long characterId = entry.getFirst();
                            final byte[] data = entry.getSecond();
                            PartyMemberInterface member = partyComp.getParty().getMember(characterId);
                            if (member == null) {
                                if (characterId < 0L) {
                                    member = new ClientCompanionPartyMember();
                                }
                                else {
                                    member = new PartyMemberModel();
                                }
                                member.addListener(new PartyMemberModelClientListener());
                                final PartyMemberSerializer serializer2 = new PartyMemberSerializer(member);
                                serializer2.getHpPart().addListener(new HpPartListener(member));
                                serializer2.unserialize(data);
                                partyComp.getParty().addMember(member);
                                final String chatMessage = WakfuTranslator.getInstance().getString("group.party.invitationAccepted", member.getName());
                                ChatManager.getInstance().pushMessage(chatMessage, 4);
                            }
                            else {
                                final PartyMemberSerializer serializer2 = new PartyMemberSerializer(member);
                                serializer2.getHpPart().addListener(new HpPartListener(member));
                                serializer2.unserialize(data);
                            }
                            needUIUpdate = true;
                        }
                        if (!wasInParty && partyComp.isInParty() && !WakfuGameEntity.getInstance().hasFrame(UIPartyFrame.getInstance())) {
                            WakfuGameEntity.getInstance().pushFrame(UIPartyFrame.getInstance());
                        }
                        else if (WakfuGameEntity.getInstance().hasFrame(UIPartyFrame.getInstance()) && needUIUpdate) {
                            UIPartyFrame.getInstance().fireUpdate();
                        }
                        PartyUtils.updateMembersAPS(partyComp.getParty());
                        if (needUIUpdate) {
                            PartyUtils.onPositionUpdate(partyComp.getParty());
                            break;
                        }
                        break;
                    }
                }
                return false;
            }
            case 502: {
                final GroupInvitationRequestMessage msg2 = (GroupInvitationRequestMessage)message;
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final UIGroupInvitationMessage invitationDialogMsg = new UIGroupInvitationMessage();
                final String inviterName = msg2.getInviterName();
                if (localPlayer.isActiveProperty(WorldPropertyType.GROUP_DISABLED) || WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserById(msg2.getInviterId()) != null) {
                    final GroupInvitationAnswerMessage answerMsg = new GroupInvitationAnswerMessage();
                    answerMsg.setInvitationAccepted(false);
                    answerMsg.setInviterName(msg2.getInviterName());
                    answerMsg.setInviterId(msg2.getInviterId());
                    answerMsg.setGroupType(msg2.getGroupType());
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(answerMsg);
                    return false;
                }
                invitationDialogMsg.setGroupType(GroupType.getFromTypeId(msg2.getGroupType()));
                invitationDialogMsg.setInviterId(msg2.getInviterId());
                invitationDialogMsg.setInviterName(inviterName);
                invitationDialogMsg.setBooleanValue(msg2.isFromPartySearch());
                Worker.getInstance().pushMessage(invitationDialogMsg);
                return false;
            }
            case 504: {
                final GroupResultMessage msg3 = (GroupResultMessage)message;
                ErrorsMessageTranslator.getInstance().pushMessage(msg3.getResult(), 3, new Object[0]);
                return false;
            }
            case 506: {
                final GroupRemovedCharacterMessage msg4 = (GroupRemovedCharacterMessage)message;
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final PartyComportment partyComp = localPlayer.getPartyComportment();
                final long groupId = msg4.getGroupId();
                final GroupType groupType2 = GroupType.getFromGroupId(groupId);
                final TLongArrayList removedIds = msg4.getCharacterRemovedIds();
                switch (groupType2) {
                    case PARTY: {
                        for (int i = 0; i < removedIds.size(); ++i) {
                            if (partyComp.isInParty()) {
                                partyComp.getParty().removeMember(removedIds.get(i));
                            }
                            else {
                                NetGroupFrame.m_logger.error((Object)"Message de retrait d'un character du groupe re\u00e7u alors qu'on ne fait partie d'aucun groupe");
                            }
                            if (localPlayer.getId() == removedIds.getQuick(i)) {
                                if (WakfuGameEntity.getInstance().hasFrame(UIPartyFrame.getInstance())) {
                                    WakfuGameEntity.getInstance().removeFrame(UIPartyFrame.getInstance());
                                }
                                for (final long characterId2 : HeroesManager.INSTANCE.getHeroesInParty(localPlayer.getOwnerId())) {
                                    if (characterId2 == localPlayer.getId()) {
                                        continue;
                                    }
                                    final LocalPlayerCharacter hero = HeroesManager.INSTANCE.getHero(characterId2);
                                    if (hero == null) {
                                        continue;
                                    }
                                    HeroesManager.INSTANCE.removeHeroFromParty(localPlayer.getOwnerId(), characterId2);
                                }
                                localPlayer.getPartyComportment().getParty().removeAllMembers();
                                localPlayer.getPartyComportment().setParty(null);
                                final String chatMessage2 = WakfuTranslator.getInstance().getString("group.party.leave");
                                ChatManager.getInstance().pushMessage(chatMessage2, 4);
                            }
                            else if (WakfuGameEntity.getInstance().hasFrame(UIPartyFrame.getInstance())) {
                                UIPartyFrame.getInstance().fireUpdate();
                            }
                        }
                        break;
                    }
                }
                return false;
            }
            case 516: {
                final GroupDestroyedMessage msg5 = (GroupDestroyedMessage)message;
                final long groupId2 = msg5.getGroupId();
                final GroupType groupType = GroupType.getFromGroupId(groupId2);
                return false;
            }
            case 508: {
                final GroupPrivateContentDispatchMessage msg6 = (GroupPrivateContentDispatchMessage)message;
                final ChatMessage chatMessage3 = new ChatMessage(msg6.getTalkingCharacter(), msg6.getTalkingId(), msg6.getMessage());
                chatMessage3.setPipeDestination(5);
                WakfuWordsModerator.controlChatMessage(chatMessage3);
                ChatManager.getInstance().pushMessage(chatMessage3);
                return false;
            }
            case 528: {
                final GuildPrivateContentDispatchMessage msg7 = (GuildPrivateContentDispatchMessage)message;
                final String talking = msg7.getTalkingCharacter();
                if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserById(msg7.getTalkingId()) != null) {
                    return false;
                }
                final ChatMessage chatMessage4 = new ChatMessage(talking, msg7.getTalkingId(), msg7.getMessage());
                chatMessage4.setPipeDestination(6);
                WakfuWordsModerator.controlChatMessage(chatMessage4);
                ChatManager.getInstance().pushMessage(chatMessage4);
                return false;
            }
            case 20054: {
                final CreateGuildAnswerMessage msg8 = (CreateGuildAnswerMessage)message;
                if (msg8.getErrorCode() == 0) {
                    if (WakfuGameEntity.getInstance().hasFrame(UIGuildCreatorFrame.getInstance())) {
                        WakfuGameEntity.getInstance().removeFrame(UIGuildCreatorFrame.getInstance());
                    }
                    return false;
                }
                Xulor.getInstance().msgBox(ErrorsMessageTranslator.getInstance().getMessageByErrorId(msg8.getErrorCode(), new Object[0]), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 2L, 102, 3);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetGroupFrame.class);
        m_instance = new NetGroupFrame();
    }
}
