package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class PoliticMessageCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        String commandLine = args.get(2);
        commandLine = ChatHelper.controlSmsAndFlood(commandLine);
        if (commandLine == null) {
            return;
        }
        commandLine = UIChatFrame.getInstance().formatMessageWithItemInfos(commandLine);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.getCitizenComportment().getRank() != null) {
            if (commandLine.startsWith("#")) {
                commandLine = commandLine.replaceFirst("#", "");
                final UserVicinityPoliticContentMessage message = new UserVicinityPoliticContentMessage();
                message.setMessageContent(commandLine);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
                commandLine = WakfuWordsModerator.makeValidSentence(commandLine);
                if (commandLine.isEmpty()) {
                    final String errorMessage = WakfuTranslator.getInstance().getString("error.chat.operationNotPermited");
                    final ChatMessage m = new ChatMessage(errorMessage);
                    m.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(m);
                }
            }
            else {
                commandLine = WakfuWordsModerator.makeValidSentence(commandLine);
                if (commandLine.isEmpty()) {
                    final String errorMessage2 = WakfuTranslator.getInstance().getString("error.chat.operationNotPermited");
                    final ChatMessage i = new ChatMessage(errorMessage2);
                    i.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(i);
                    return;
                }
                final UserPoliticContentMessage message2 = new UserPoliticContentMessage();
                message2.setMessageContent(commandLine);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message2);
            }
        }
        else {
            final String chatMessage = WakfuTranslator.getInstance().getString("unknown");
            ChatManager.getInstance().pushMessage(chatMessage, 3);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
