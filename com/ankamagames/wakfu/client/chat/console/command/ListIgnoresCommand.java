package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.chat.*;
import java.util.*;

public class ListIgnoresCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final ContactListCategory ignoreGroup = WakfuUserGroupManager.getInstance().getIgnoreGroup();
        if (ignoreGroup != null) {
            String messageText = WakfuTranslator.getInstance().getString("chat.ignoreList");
            messageText += " :\n";
            final StringBuilder s = new StringBuilder("");
            for (final WakfuUser u : ignoreGroup) {
                s.append(" +").append(u.getName()).append(" (");
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
