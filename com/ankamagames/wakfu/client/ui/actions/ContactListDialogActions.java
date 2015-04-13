package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.chat.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.protocol.message.group.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class ContactListDialogActions
{
    public static final String PACKAGE = "wakfu.contactList";
    
    public static void openContactPopupMenu(final ItemEvent event) {
        if (event.getButton() == 3) {
            final Object value = event.getItemValue();
            if (value != null && value instanceof WakfuUser) {
                final UIFriendUserMessage message = new UIFriendUserMessage();
                message.setUser((WakfuUser)value);
                message.setId(19003);
                Worker.getInstance().pushMessage(message);
            }
        }
    }
    
    public static void closeContactList(final Event e) {
        UIMessage.send((short)19002);
    }
    
    public static void checkNotify(final Event event, final WakfuUser friend) {
        if (friend != null) {
            final UIFriendUserMessage message = new UIFriendUserMessage();
            message.setUser(friend);
            message.setId(19005);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void enableDisableFriendFilter(final Event event) {
        UIMessage.send((short)19009);
    }
    
    public static void enableDisableIgnoreFilter(final Event event) {
        UIMessage.send((short)19010);
    }
    
    public static void addFriend(final Event event) {
        if (event.getType() == Events.MOUSE_CLICKED || (event.getType() == Events.KEY_PRESSED && ((KeyEvent)event).getKeyCode() == 10)) {
            addRemove(event, (short)1, true);
        }
    }
    
    public static void addIgnore(final Event event) {
        if (event.getType() == Events.MOUSE_CLICKED || (event.getType() == Events.KEY_PRESSED && ((KeyEvent)event).getKeyCode() == 10)) {
            addRemove(event, (short)2, true);
        }
    }
    
    public static void removeIgnore(final Event e) {
        addRemove(e, (short)2, false);
    }
    
    public static void addToGroup(final Event e) {
        addRemove(e, (short)4, true);
    }
    
    private static void addRemove(final Event e, final short type, final boolean add) {
        final UIAddRemoveUsersFromContactsMessage msg = new UIAddRemoveUsersFromContactsMessage();
        msg.setId(add ? 19020 : 19021);
        msg.setType(type);
        final ElementMap map = e.getTarget().getElementMap();
        final TextEditor ed = (TextEditor)map.getElement((type == 1) ? "customFriendName" : "customIgnoreName");
        if (ed != null) {
            final String customName = ed.getText();
            if (customName == null || customName.isEmpty()) {
                return;
            }
            WakfuUser user = WakfuUserGroupManager.getInstance().getUser(customName);
            if (user == null) {
                user = WakfuUserGroupManager.getInstance().getUserByCharacterName(customName);
                if (user == null) {
                    user = new WakfuUser(-1L, customName, customName);
                }
            }
            msg.setAddedUser(user);
            ed.setText("");
        }
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void selectUnselectUser(final SelectionChangedEvent event, final Short type) {
        final String name = event.getTarget().getId();
        final WakfuUser user = WakfuUserGroupManager.getInstance().getUser(name);
        if (user == null) {
            return;
        }
        final UIFriendUserMessage msg = new UIFriendUserMessage();
        msg.setUser(user);
        msg.setShortValue(type);
        if (event.isSelected()) {
            msg.setId(19016);
        }
        else {
            msg.setId(19017);
        }
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void addCommentary(final Event event, final WakfuUser user) {
        if (user == null) {
            return;
        }
        final UIFriendUserMessage msg = new UIFriendUserMessage();
        msg.setUser(user);
        msg.setId(19060);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void validCommentary(final Event event, final WakfuUser user, final TextEditor textEditor) {
        if (user == null) {
            return;
        }
        if (event.getType() == Events.MOUSE_CLICKED || (event.getType() == Events.KEY_PRESSED && ((KeyEvent)event).getKeyCode() == 10)) {
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("contactListDialog");
            final UIFriendUserMessage msg = new UIFriendUserMessage();
            msg.setUser(user);
            final String text = textEditor.getText();
            final String moderatedText = WakfuWordsModerator.makeValidSentence(text);
            textEditor.setText("");
            if (moderatedText.length() == 0 && text.length() != 0) {
                msgBox(WakfuTranslator.getInstance().getString("error.censoredSentence"), textEditor);
                return;
            }
            msg.setStringValue(moderatedText);
            msg.setId(19028);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    private static void msgBox(final String msg, final TextEditor te) {
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(msg, WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                FocusManager.getInstance().setFocused(te);
            }
        });
    }
    
    public static void playListSound(final boolean expand) {
        WakfuSoundManager.getInstance().playGUISound(expand ? 600117L : 600118L);
    }
    
    public static void showIgnored(final Event event) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("contactListDialog");
        Widget list = (Widget)map.getElement("ignoreList");
        final boolean show = !list.getVisible();
        list.setVisible(show);
        Widget bouton = (Widget)map.getElement("ignoreButton");
        if (show) {
            bouton.setStyle("remove");
        }
        else {
            bouton.setStyle("add");
        }
        list = (Widget)map.getElement("friendsOfflineList");
        list.setVisible(!show);
        bouton = (Widget)map.getElement("friendsOfflineButton");
        if (!show) {
            bouton.setStyle("remove");
        }
        else {
            bouton.setStyle("add");
        }
        playListSound(show);
    }
    
    public static void showOffline(final Event event) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("contactListDialog");
        Widget list = (Widget)map.getElement("friendsOfflineList");
        final boolean show = !list.getVisible();
        list.setVisible(show);
        Widget bouton = (Widget)map.getElement("friendsOfflineButton");
        if (show) {
            bouton.setStyle("remove");
        }
        else {
            bouton.setStyle("add");
        }
        list = (Widget)map.getElement("ignoreList");
        list.setVisible(!show);
        bouton = (Widget)map.getElement("ignoreButton");
        if (!show) {
            bouton.setStyle("remove");
        }
        else {
            bouton.setStyle("add");
        }
        playListSound(show);
    }
    
    public static void removeFriend(final Event event, final WakfuUser user) {
        final UIAddRemoveUsersFromContactsMessage msg = new UIAddRemoveUsersFromContactsMessage();
        msg.setId(19021);
        msg.setType((short)1);
        msg.setAddedUser(user);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void addFriendToGroup(final Event event, final WakfuUser user) {
        final UIGroupSendInvitationMessage msg = new UIGroupSendInvitationMessage();
        msg.setGroupType(GroupType.PARTY);
        msg.setName(user.getCharacterName());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void removeIgnore(final Event event, final WakfuUser user) {
        final UIAddRemoveUsersFromContactsMessage msg = new UIAddRemoveUsersFromContactsMessage();
        msg.setId(19021);
        msg.setType((short)2);
        msg.setAddedUser(user);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void displayDisconnectedPlayers(final Event event) {
        WakfuUserGroupManager.getInstance().setDisplayDisconnectedPlayers(event.getCurrentTarget().getSelected());
    }
}
