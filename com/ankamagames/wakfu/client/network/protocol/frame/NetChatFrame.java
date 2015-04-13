package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient.errorMessage.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.chat.*;
import java.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.userGroup.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.sound.*;

public class NetChatFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static NetChatFrame m_instance;
    
    public static NetChatFrame getInstance() {
        return NetChatFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 3140: {
                final ChannelContentMessage msg = (ChannelContentMessage)message;
                final String talking = msg.getMemberTalking();
                if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserByCharacterName(talking) != null) {
                    return false;
                }
                final ChatMessage chatMessage = new ChatMessage(talking, msg.getMessageContent());
                final String channelName = msg.getChannelName();
                boolean createASubPipe = true;
                int pipeDestination = -1;
                if ("admin_channel".equals(channelName)) {
                    pipeDestination = 10;
                    createASubPipe = false;
                }
                else if ("all_channel".equals(channelName)) {
                    pipeDestination = 11;
                    createASubPipe = false;
                }
                else if (channelName != null && channelName.startsWith("guild_")) {
                    pipeDestination = 6;
                    createASubPipe = false;
                }
                chatMessage.setPipeDestination(pipeDestination);
                WakfuWordsModerator.controlChatMessage(chatMessage);
                if (chatMessage.getMessage().length() == 0) {
                    return false;
                }
                if (createASubPipe) {
                    ChatManager.getInstance().pushMessage(chatMessage, channelName);
                }
                else {
                    ChatManager.getInstance().pushMessage(chatMessage);
                }
                return false;
            }
            case 3178: {
                final ModeratorChatCreationMessage msg2 = (ModeratorChatCreationMessage)message;
                final String remoteUser = msg2.getRemoteUser();
                final long sourceId = msg2.getSourceAccountId();
                final String firstMsg = msg2.getMessage();
                UIModeratorChatFrame.getInstance().createChat(remoteUser, sourceId, firstMsg);
                return false;
            }
            case 3180: {
                final ModeratorNumRequestUpdateMessage msg3 = (ModeratorNumRequestUpdateMessage)message;
                final int numRequester = msg3.getNumRequester();
                this.setNumModeratorRequest(numRequester);
                return false;
            }
            case 3182: {
                final ModeratorRequestClosedMessage msg4 = (ModeratorRequestClosedMessage)message;
                final AbstractUIMessage uimsg = new UIMessage((short)19071);
                uimsg.setByteValue(msg4.getReason());
                Worker.getInstance().pushMessage(uimsg);
                return false;
            }
            case 3222: {
                final HasModerationRequestMessage msg5 = (HasModerationRequestMessage)message;
                final boolean hasRequests = msg5.hasRequests();
                PropertiesProvider.getInstance().setPropertyValue("hasModeratorChatRequest", !hasRequests);
                return false;
            }
            case 3220: {
                final ModeratorRequestErrorMessage msg6 = (ModeratorRequestErrorMessage)message;
                String translatorKey = "unknown";
                switch (msg6.getErrorId()) {
                    case 2: {
                        translatorKey = "contactModerator.noRequest";
                        break;
                    }
                    case 1: {
                        translatorKey = "contactModerator.contactdisconnected";
                        break;
                    }
                    case 3: {
                        translatorKey = "contactModerator.error.ignored";
                        break;
                    }
                }
                final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString(translatorKey));
                chatMessage.setPipeDestination(3);
                ChatManager.getInstance().pushMessage(chatMessage);
                return false;
            }
            case 3156: {
                final FriendAddedMessage msg7 = (FriendAddedMessage)message;
                final WakfuUser user = new WakfuUser(msg7.getCharacterName(), msg7.getFriendName(), true, msg7.getFriendId(), true, msg7.getFriendBreedId(), msg7.getFriendSex(), "", msg7.getFriendXp());
                WakfuUserGroupManager.getInstance().addUser((short)1, user);
                final TextWidgetFormater textWidgetFormater = new TextWidgetFormater();
                ChatView.createLink(textWidgetFormater, "characterName_" + msg7.getFriendId(), msg7.getCharacterName());
                final String notifyText = WakfuTranslator.getInstance().getString("chat.notify.addFriend", textWidgetFormater.finishAndToString(), msg7.getFriendName());
                final ChatMessage notifyMessage = new ChatMessage(notifyText);
                notifyMessage.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(notifyMessage);
                return false;
            }
            case 3160: {
                final FriendRemovedMessage msg8 = (FriendRemovedMessage)message;
                if (!WakfuUserGroupManager.getInstance().removeUser((short)1, msg8.getFriendName())) {
                    final String notifyError = WakfuTranslator.getInstance().getString("error.chat.userNotFound", msg8.getFriendName());
                    final ChatMessage notifyErrorMessage = new ChatMessage(notifyError);
                    notifyErrorMessage.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(notifyErrorMessage);
                }
                else {
                    final String notifyText2 = WakfuTranslator.getInstance().getString("chat.notify.removeFriend", msg8.getFriendName());
                    final ChatMessage notifyMessage2 = new ChatMessage(notifyText2);
                    notifyMessage2.setPipeDestination(4);
                    ChatManager.getInstance().pushMessage(notifyMessage2);
                }
                return false;
            }
            case 3148: {
                final NotificationFriendOnlineMessage msg9 = (NotificationFriendOnlineMessage)message;
                final ContactListCategory friendGroup = WakfuUserGroupManager.getInstance().getFriendGroup();
                if (friendGroup != null) {
                    WakfuUser friend = friendGroup.getUserById(msg9.getUserId());
                    if (friend == null) {
                        friend = friendGroup.getUser(msg9.getFriendName());
                    }
                    if (friend == null) {
                        NetChatFrame.m_logger.error((Object)("Ami inconnu " + msg9.getFriendName()));
                        final String notifyError2 = WakfuTranslator.getInstance().getString("error.chat.userNotFound", msg9.getFriendName());
                        final ChatMessage notifyErrorMessage2 = new ChatMessage(notifyError2);
                        notifyErrorMessage2.setPipeDestination(3);
                        ChatManager.getInstance().pushMessage(notifyErrorMessage2);
                        return false;
                    }
                    friend.setCharacterName(msg9.getCharacterName());
                    friend.setOnline(true);
                    friend.setId(msg9.getUserId());
                    friend.setComentary(msg9.getCommentary());
                    friend.setBreedId(msg9.getBreedId());
                    friend.setSex(msg9.getSex());
                    friend.setXp(msg9.getXp());
                    if (friend.isNotify()) {
                        final TextWidgetFormater textWidgetFormater2 = new TextWidgetFormater();
                        ChatView.createLink(textWidgetFormater2, "characterName_" + msg9.getUserId(), msg9.getCharacterName());
                        final String notifyText3 = WakfuTranslator.getInstance().getString("chat.notify.friendOnline", textWidgetFormater2.finishAndToString(), msg9.getFriendName());
                        final ChatMessage notifyMessage3 = new ChatMessage(notifyText3);
                        notifyMessage3.setPipeDestination(4);
                        ChatManager.getInstance().pushMessage(notifyMessage3);
                    }
                    friend.updateProperty();
                }
                return false;
            }
            case 3150: {
                final NotificationFriendOfflineMessage msg10 = (NotificationFriendOfflineMessage)message;
                final ContactListCategory friendGroup = WakfuUserGroupManager.getInstance().getFriendGroup();
                if (friendGroup != null) {
                    WakfuUser friend = friendGroup.getUserById(msg10.getUserId());
                    if (friend == null) {
                        friend = friendGroup.getUser(msg10.getFriendName());
                    }
                    if (friend != null) {
                        friend.setId(msg10.getUserId());
                        friend.setCharacterName("");
                        friend.setOnline(false);
                        friend.setBreedId((short)(-1));
                        friend.setXp(-1L);
                        if (friend.isNotify()) {
                            final String notifyText = WakfuTranslator.getInstance().getString("chat.notify.friendOffline", msg10.getCharacterName(), msg10.getFriendName());
                            final ChatMessage notifyMessage = new ChatMessage(notifyText);
                            notifyMessage.setPipeDestination(4);
                            ChatManager.getInstance().pushMessage(notifyMessage);
                        }
                        PropertiesProvider.getInstance().firePropertyValueChanged(friend, friend.getFields());
                        PropertiesProvider.getInstance().firePropertyValueChanged(friendGroup, "contentList");
                    }
                    else {
                        NetChatFrame.m_logger.error((Object)("Ami inconnu " + msg10.getFriendName()));
                    }
                }
                return false;
            }
            case 3144: {
                final FriendListMessage msg11 = (FriendListMessage)message;
                final ArrayList<WakfuUser> friendList = new ArrayList<WakfuUser>();
                for (final FriendListMessage.FriendInformation friendInformation : msg11.getFriendInformationList()) {
                    friendList.add(new WakfuUser(friendInformation.characterName, friendInformation.name, friendInformation.userId != -1L, friendInformation.userId, friendInformation.notify, friendInformation.breedId, friendInformation.sex, friendInformation.commentary, friendInformation.xp));
                }
                WakfuUserGroupManager.getInstance().addUsers((short)1, friendList);
                return false;
            }
            case 3158: {
                final IgnoreAddedMessage msg12 = (IgnoreAddedMessage)message;
                final WakfuUser user = new WakfuUser(msg12.getIgnoreId(), msg12.getIgnoreName(), msg12.getCharacterName());
                WakfuUserGroupManager.getInstance().addUser((short)2, user);
                final String notifyText4 = WakfuTranslator.getInstance().getString("chat.notify.addIgnore", msg12.getCharacterName(), msg12.getIgnoreName());
                final ChatMessage notifyMessage4 = new ChatMessage(notifyText4);
                notifyMessage4.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(notifyMessage4);
                return false;
            }
            case 3162: {
                final IgnoreRemovedMessage msg13 = (IgnoreRemovedMessage)message;
                if (!WakfuUserGroupManager.getInstance().removeUser((short)2, msg13.getIgnoreName())) {
                    final String notifyError = WakfuTranslator.getInstance().getString("error.chat.userNotFound", msg13.getIgnoreName());
                    final ChatMessage notifyErrorMessage = new ChatMessage(notifyError);
                    notifyErrorMessage.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(notifyErrorMessage);
                }
                else {
                    final String notifyText2 = WakfuTranslator.getInstance().getString("chat.notify.removeIgnore", msg13.getIgnoreName());
                    final ChatMessage notifyMessage2 = new ChatMessage(notifyText2);
                    notifyMessage2.setPipeDestination(4);
                    ChatManager.getInstance().pushMessage(notifyMessage2);
                }
                return false;
            }
            case 3164: {
                final NotificationIgnoreOnlineMessage msg14 = (NotificationIgnoreOnlineMessage)message;
                final ContactListCategory ignoreGroup = WakfuUserGroupManager.getInstance().getFriendGroup();
                if (ignoreGroup != null) {
                    WakfuUser ignore = ignoreGroup.getUserById(msg14.getUserId());
                    if (ignore == null) {
                        ignore = ignoreGroup.getUser(msg14.getIgnoreName());
                    }
                    if (ignore != null) {
                        ignore.setCharacterName(msg14.getIgnoreCharacterName());
                        ignore.setOnline(true);
                        ignore.setId(msg14.getUserId());
                    }
                    else {
                        NetChatFrame.m_logger.error((Object)("Ignor\u00e9 inconnu " + msg14.getIgnoreName()));
                    }
                }
                return false;
            }
            case 3166: {
                final NotificationIgnoreOfflineMessage msg15 = (NotificationIgnoreOfflineMessage)message;
                final ContactListCategory ignoreGroup = WakfuUserGroupManager.getInstance().getFriendGroup();
                if (ignoreGroup != null) {
                    User ignore2 = ignoreGroup.getUserById(msg15.getUserId());
                    if (ignore2 == null) {
                        ignore2 = ignoreGroup.getUser(msg15.getIgnoreName());
                    }
                    if (ignore2 != null) {
                        ignore2.setId(msg15.getUserId());
                        ignore2.setOnline(false);
                    }
                    else {
                        NetChatFrame.m_logger.error((Object)("Ignor\u00e9 inconnu " + msg15.getIgnoreName()));
                    }
                }
                return false;
            }
            case 3146: {
                final IgnoreListMessage msg16 = (IgnoreListMessage)message;
                final ArrayList<WakfuUser> ignoreList = new ArrayList<WakfuUser>();
                for (final ObjectPair<Long, ObjectPair<String, String>> ignore3 : msg16.getIgnoreList()) {
                    final ObjectPair<String, String> namePair = ignore3.getSecond();
                    ignoreList.add(new WakfuUser(ignore3.getFirst(), namePair.getFirst(), namePair.getSecond()));
                }
                WakfuUserGroupManager.getInstance().addUsers((short)2, ignoreList);
                return false;
            }
            case 3154: {
                final PrivateContentMessage msg17 = (PrivateContentMessage)message;
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final long localOwnerId = localPlayer.getClientId();
                final long talkingId = (localOwnerId == msg17.getMemberIDTalking()) ? localPlayer.getId() : msg17.getMemberIDTalking();
                final String talking2 = (localOwnerId == msg17.getMemberIDTalking()) ? msg17.getMemberTalked() : msg17.getMemberTalking();
                if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserByCharacterName(talking2) != null) {
                    return false;
                }
                final ChatMessage chatMessage2 = new ChatMessage(talking2, talkingId, msg17.getMessageContent());
                chatMessage2.setPipeDestination(2);
                WakfuWordsModerator.controlChatMessage(chatMessage2);
                if (chatMessage2.getMessage().isEmpty()) {
                    return false;
                }
                ChatManager.getInstance().pushMessage(chatMessage2, talking2);
                ChatManager.getInstance().addLastPrivateContact(talking2);
                this.playPrivateSound();
                return false;
            }
            case 3300: {
                final RedModerationMessage msg18 = (RedModerationMessage)message;
                String name = msg18.getMemberTalking();
                final boolean isTradkey = msg18.isTradkey();
                final boolean isPrivate = msg18.isPrivate();
                final String messageContent = isTradkey ? WakfuTranslator.getInstance().getString(msg18.getMessageContent()) : msg18.getMessageContent();
                final long id = msg18.getMemberIDTalking();
                if (name.isEmpty()) {
                    final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(id);
                    if (character != null) {
                        name = character.getName();
                    }
                    else {
                        name = "UNDEFINED";
                    }
                }
                ChatMessage chatMessage2;
                if (isPrivate) {
                    final TextWidgetFormater formattedMessage = new TextWidgetFormater();
                    final float[] colorArray = ChatManager.getInstance().getChatPipe(3).getColor();
                    final String color = XulorUtil.convertToTextBuilderColor(colorArray);
                    convert(formattedMessage, "characterName_" + id, name, null, color, messageContent);
                    chatMessage2 = new ChatMessage(name, id, formattedMessage.finishAndToString());
                }
                else {
                    chatMessage2 = new ChatMessage("[Zone] " + name + " : " + messageContent);
                }
                chatMessage2.setPipeDestination(3);
                if (chatMessage2.getMessage().isEmpty()) {
                    return false;
                }
                ChatManager.getInstance().pushMessage(chatMessage2);
                if (msg18.getMessageContent().toUpperCase().contains(WakfuGameEntity.getInstance().getLocalPlayer().getName().toUpperCase())) {
                    this.playPrivateSound();
                }
                return false;
            }
            case 3152: {
                final VicinityContentMessage msg19 = (VicinityContentMessage)message;
                String name = msg19.getMemberTalking();
                if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserByCharacterName(name) != null) {
                    return false;
                }
                final long id2 = msg19.getMemberIDTalking();
                if (name.isEmpty()) {
                    final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(id2);
                    if (character2 != null) {
                        name = character2.getName();
                    }
                    else {
                        name = "UNDEFFINED";
                    }
                }
                final ChatMessage chatMessage3 = new ChatMessage(name, id2, msg19.getMessageContent());
                chatMessage3.setPipeDestination(1);
                WakfuWordsModerator.controlChatMessage(chatMessage3);
                if (chatMessage3.getMessage().length() == 0) {
                    return false;
                }
                ChatManager.getInstance().pushMessage(chatMessage3);
                if (msg19.getMessageContent().toUpperCase().contains(WakfuGameEntity.getInstance().getLocalPlayer().getName().toUpperCase())) {
                    this.playPrivateSound();
                }
                return false;
            }
            case 3168: {
                final TradeContentMessage msg20 = (TradeContentMessage)message;
                if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserByCharacterName(msg20.getMemberTalking()) != null) {
                    return false;
                }
                String name = msg20.getMemberTalking();
                final long id2 = msg20.getMemberIDTalking();
                if (name.isEmpty()) {
                    final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(id2);
                    if (character2 != null) {
                        name = character2.getName();
                    }
                    else {
                        name = "UNDEFFINED";
                    }
                }
                final ChatMessage chatMessage3 = new ChatMessage(name, id2, msg20.getMessageContent());
                chatMessage3.setPipeDestination(7);
                WakfuWordsModerator.controlChatMessage(chatMessage3);
                if (chatMessage3.getMessage().length() == 0) {
                    return false;
                }
                ChatManager.getInstance().pushMessage(chatMessage3);
                return false;
            }
            case 3176: {
                final RecruteContentMessage msg21 = (RecruteContentMessage)message;
                if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserByCharacterName(msg21.getMemberTalking()) != null) {
                    return false;
                }
                String name = msg21.getMemberTalking();
                final long id2 = msg21.getMemberIDTalking();
                if (name.isEmpty()) {
                    final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(id2);
                    if (character2 != null) {
                        name = character2.getName();
                    }
                    else {
                        name = "UNDEFFINED";
                    }
                }
                final ChatMessage chatMessage3 = new ChatMessage(name, id2, msg21.getMessageContent());
                chatMessage3.setPipeDestination(12);
                WakfuWordsModerator.controlChatMessage(chatMessage3);
                if (chatMessage3.getMessage().length() == 0) {
                    return false;
                }
                ChatManager.getInstance().pushMessage(chatMessage3);
                return false;
            }
            case 3174: {
                final VicinityPoliticContentMessage msg22 = (VicinityPoliticContentMessage)message;
                if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserByCharacterName(msg22.getMemberTalking()) != null) {
                    return false;
                }
                String name = msg22.getMemberTalking();
                final long id2 = msg22.getMemberIDTalking();
                if (name.isEmpty()) {
                    final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(id2);
                    if (character2 != null) {
                        name = character2.getName();
                    }
                    else {
                        name = "UNDEFFINED";
                    }
                }
                final ChatMessage chatMessage3 = new ChatMessage(name, id2, msg22.getMessageContent());
                chatMessage3.setPipeDestination(8);
                WakfuWordsModerator.controlChatMessage(chatMessage3);
                if (chatMessage3.getMessage().length() == 0) {
                    return false;
                }
                ChatManager.getInstance().pushMessage(chatMessage3);
                this.playPoliticSound();
                return false;
            }
            case 3170: {
                final TeamContentMessage msg23 = (TeamContentMessage)message;
                final String talking = msg23.getMemberTalking();
                if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserByCharacterName(talking) != null) {
                    return false;
                }
                String name2 = msg23.getMemberTalking();
                final long id3 = msg23.getMemberIDTalking();
                if (name2.isEmpty()) {
                    final CharacterInfo character3 = CharacterInfoManager.getInstance().getCharacter(id3);
                    if (character3 != null) {
                        name2 = character3.getName();
                    }
                    else {
                        name2 = "UNDEFFINED";
                    }
                }
                final ChatMessage chatMessage4 = new ChatMessage(name2, id3, msg23.getMessageContent());
                chatMessage4.setPipeDestination(9);
                WakfuWordsModerator.controlChatMessage(chatMessage4);
                if (chatMessage4.getMessage().length() == 0) {
                    return false;
                }
                ChatManager.getInstance().pushMessage(chatMessage4);
                return false;
            }
            case 3172: {
                final PoliticContentMessage msg24 = (PoliticContentMessage)message;
                final String talking = msg24.getMemberTalking();
                if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserByCharacterName(talking) != null) {
                    return false;
                }
                final ChatMessage chatMessage = new ChatMessage(msg24.getMemberTalking(), msg24.getMemberIDTalking(), msg24.getMessageContent());
                chatMessage.setPipeDestination(8);
                WakfuWordsModerator.controlChatMessage(chatMessage);
                if (chatMessage.getMessage().length() == 0) {
                    return false;
                }
                ChatManager.getInstance().pushMessage(chatMessage);
                if (msg24.getMemberIDTalking() != WakfuGameEntity.getInstance().getLocalPlayer().getId()) {
                    this.playPoliticSound();
                }
                return false;
            }
            case 3202: {
                final ChannelNotFoundMessage msg25 = (ChannelNotFoundMessage)message;
                final String errorMessage = WakfuTranslator.getInstance().getString("error.chat.channelNotFound", msg25.getChannelName());
                final ChatMessage chatMessage = new ChatMessage(errorMessage);
                chatMessage.setPipeDestination(3);
                ChatManager.getInstance().pushMessage(chatMessage);
                return false;
            }
            case 3214: {
                final String errorMessage2 = WakfuTranslator.getInstance().getString("error.chat.targetIsYourself");
                final ChatMessage chatMessage5 = new ChatMessage(errorMessage2);
                chatMessage5.setPipeDestination(3);
                ChatManager.getInstance().pushMessage(chatMessage5);
                return false;
            }
            case 3204: {
                final UserNotFoundMessage msg26 = (UserNotFoundMessage)message;
                final String errorMessage = WakfuTranslator.getInstance().getString("error.chat.userNotFound", msg26.getUserName());
                final ChatMessage chatMessage = new ChatMessage(errorMessage);
                chatMessage.setPipeDestination(3);
                final ChatManager chatManager = ChatManager.getInstance();
                chatManager.pushMessage(chatMessage);
                return false;
            }
            case 3218: {
                final UserIgnoreYouMessage msg27 = (UserIgnoreYouMessage)message;
                final String errorMessage = WakfuTranslator.getInstance().getString("error.chat.userIgnoreYou", msg27.getUserName());
                final ChatMessage chatMessage = new ChatMessage(errorMessage);
                chatMessage.setPipeDestination(3);
                final ChatManager chatManager = ChatManager.getInstance();
                chatManager.pushMessage(chatMessage);
                return false;
            }
            case 3216: {
                final String errorMessage2 = WakfuTranslator.getInstance().getString("error.chat.operationNotPermited");
                final ChatMessage chatMessage5 = new ChatMessage(errorMessage2);
                chatMessage5.setPipeDestination(3);
                ChatManager.getInstance().pushMessage(chatMessage5);
                return false;
            }
            case 3221: {
                final WorldPropertyTypeErrorMessage msg28 = (WorldPropertyTypeErrorMessage)message;
                final short id4 = msg28.getErrorId();
                ChatWorldPropertyTypeErrorManager.writeChatErrorMessage(id4);
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
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    private void playPoliticSound() {
        WakfuSoundManager.getInstance().playGUISound(600109L);
    }
    
    private void playPrivateSound() {
        WakfuSoundManager.getInstance().playGUISound(600145L);
    }
    
    public void setNumModeratorRequest(final int numRequests) {
        PropertiesProvider.getInstance().setPropertyValue("numModeratorHelpRequestText", "Requests:" + numRequests);
    }
    
    private static void convert(final TextWidgetFormater formattedMessage, final String sourceId, final String sourceName, final String pipeName, final String color, final String messageContent) {
        if (color != null) {
            formattedMessage.openText();
            formattedMessage.addColor(color);
        }
        if (pipeName != null) {
            formattedMessage.append("[").append(pipeName).append("] ");
        }
        if (sourceName != null) {
            createLink(formattedMessage, sourceId, sourceName);
        }
        formattedMessage.append(WakfuTranslator.getInstance().getString("colon")).append(messageContent);
        if (color != null) {
            formattedMessage.closeText();
        }
    }
    
    public static void createLink(final TextWidgetFormater formattedMessage, final String sourceId, final String sourceName) {
        formattedMessage.b().u();
        formattedMessage.addId(sourceId);
        formattedMessage.append(sourceName);
        formattedMessage._u()._b();
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetChatFrame.class);
        NetChatFrame.m_instance = new NetChatFrame();
    }
}
