package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.chat.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;

public class UIContactListFrame implements MessageFrame, BigDialogLoadListener
{
    private static UIContactListFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIContactListFrame getInstance() {
        return UIContactListFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19003: {
                final UIFriendUserMessage msg = (UIFriendUserMessage)message;
                final WakfuUser user = msg.getUser();
                return false;
            }
            case 19005: {
                final UIFriendUserMessage msg = (UIFriendUserMessage)message;
                final WakfuUser friend = msg.getUser();
                if (friend != null) {
                    friend.setNotify(!friend.isNotify());
                }
                return false;
            }
            case 19020: {
                final UIAddRemoveUsersFromContactsMessage msg2 = (UIAddRemoveUsersFromContactsMessage)message;
                final WakfuUser user = msg2.getAddedUser();
                switch (msg2.getType()) {
                    case 1: {
                        if (WakfuUserGroupManager.getInstance().getFriendGroup().getUsersCount() >= 100) {
                            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("notification.groupLimitReached"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 513L, 102, 1);
                            return false;
                        }
                        if (WakfuUserGroupManager.getInstance().getFriendGroup().getUser(user) != null) {
                            final String chatMsg = WakfuTranslator.getInstance().getString("contactList.error.alreadyInFriendList", user.getCharacterName());
                            ChatManager.getInstance().pushMessage(chatMsg, 3);
                            return false;
                        }
                        if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUser(user) != null) {
                            final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("contactList.question.friendToIgnored"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
                            controler.addEventListener(new MessageBoxEventListener() {
                                @Override
                                public void messageBoxClosed(final int type, final String userEntry) {
                                    if (type == 8) {
                                        final RemoveIgnoreMessage rim = new RemoveIgnoreMessage();
                                        rim.setIgnoreName(user.getName());
                                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(rim);
                                        final AddFriendMessage afm = new AddFriendMessage();
                                        afm.setFriendName(user.getCharacterName());
                                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(afm);
                                    }
                                }
                            });
                            break;
                        }
                        final AddFriendMessage afm = new AddFriendMessage();
                        afm.setFriendName(user.getCharacterName());
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(afm);
                        break;
                    }
                    case 2: {
                        if (WakfuUserGroupManager.getInstance().getFriendGroup().getUsersCount() >= 100) {
                            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("notification.groupLimitReached"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 513L, 102, 1);
                            return false;
                        }
                        if (WakfuUserGroupManager.getInstance().getFriendGroup().getUser(user) != null) {
                            final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("contactList.question.ignoredToFriend"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
                            controler.addEventListener(new MessageBoxEventListener() {
                                @Override
                                public void messageBoxClosed(final int type, final String userEntry) {
                                    if (type == 8) {
                                        final RemoveFriendMessage rfm = new RemoveFriendMessage();
                                        rfm.setFriendName(user.getName());
                                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(rfm);
                                        final AddIgnoreMessage aim = new AddIgnoreMessage();
                                        aim.setIgnoreName(user.getCharacterName());
                                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(aim);
                                    }
                                }
                            });
                            break;
                        }
                        final AddIgnoreMessage aim = new AddIgnoreMessage();
                        aim.setIgnoreName(user.getCharacterName());
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(aim);
                        break;
                    }
                    case 4: {
                        WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment().inviteSomeone(user.getCharacterName());
                        break;
                    }
                }
                return false;
            }
            case 19021: {
                final UIAddRemoveUsersFromContactsMessage msg2 = (UIAddRemoveUsersFromContactsMessage)message;
                final WakfuUser user = msg2.getAddedUser();
                switch (msg2.getType()) {
                    case 1: {
                        final RemoveFriendMessage rfm = new RemoveFriendMessage();
                        rfm.setFriendName(user.getName());
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(rfm);
                        break;
                    }
                    case 2: {
                        final RemoveIgnoreMessage rim = new RemoveIgnoreMessage();
                        rim.setIgnoreName(user.getName());
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(rim);
                        break;
                    }
                }
                return false;
            }
            case 19060: {
                final UIFriendUserMessage uiFriendUserMessage = (UIFriendUserMessage)message;
                final WakfuUser user = uiFriendUserMessage.getUser();
                user.setComentaryEdition(true);
                PropertiesProvider.getInstance().firePropertyValueChanged(user, "commentaryEdition");
                return false;
            }
            case 19028: {
                final UIFriendUserMessage uiFriendUserMessage = (UIFriendUserMessage)message;
                final String commentary = uiFriendUserMessage.getStringValue();
                final WakfuUser user2 = uiFriendUserMessage.getUser();
                user2.setComentary(commentary);
                user2.setComentaryEdition(false);
                PropertiesProvider.getInstance().firePropertyValueChanged(user2, "commentaryEdition", "commentary");
                final AddCommentaryToContactMessage addCommentaryToContactMessage = new AddCommentaryToContactMessage();
                addCommentaryToContactMessage.setFriendName(user2.getCharacterName());
                addCommentaryToContactMessage.setCommentary(commentary);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(addCommentaryToContactMessage);
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
        if (!isAboutToBeAdded) {
            WakfuUserGroupManager.getInstance().setDisplayDisconnectedPlayers(true);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("contactListDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIContactListFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("contact.list", WakfuUserGroupManager.getInstance());
            Xulor.getInstance().load("contactListDialog", Dialogs.getDialogPath("contactListDialog"), 32768L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.contactList", ContactListDialogActions.class);
            WakfuSoundManager.getInstance().windowFadeIn();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            WakfuUserGroupManager.getInstance().getFriendGroup().cancelEditions();
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("contactListDialog");
            PropertiesProvider.getInstance().removeProperty("contact.list");
            Xulor.getInstance().removeActionClass("wakfu.contactList");
            WakfuSoundManager.getInstance().windowFadeOut();
        }
    }
    
    @Override
    public void dialogLoaded(final String id) {
        if (id != null && !id.equals("contactListDialog")) {
            WakfuGameEntity.getInstance().removeFrame(this);
        }
    }
    
    static {
        UIContactListFrame.m_instance = new UIContactListFrame();
    }
}
