package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.chat.*;
import java.util.*;

public class ListFriendsCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final ContactListCategory friendGroup = WakfuUserGroupManager.getInstance().getFriendGroup();
        if (friendGroup != null) {
            String messageText = WakfuTranslator.getInstance().getString("chat.friendList");
            messageText += " :\n";
            final StringBuilder s = new StringBuilder("");
            for (final WakfuUser u : friendGroup) {
                s.append(" +").append(u.getName()).append(" (");
                if (u.isOnline()) {
                    s.append(WakfuTranslator.getInstance().getString("online"));
                }
                else {
                    s.append(WakfuTranslator.getInstance().getString("offline"));
                }
                s.append(")\n");
            }
            messageText += s.toString();
            final ChatMessage message = new ChatMessage(messageText);
            message.setPipeDestination(4);
            ChatManager.getInstance().pushMessage(message);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
