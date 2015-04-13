package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class RecruteMessageCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        String commandLine = args.get(0).trim();
        final String command = ChatConfigurator.getCommandForChannelName(ChatCommandsParameters.RECRUTE.getCommandName()).getCommand();
        commandLine = ChatHelper.checkForCommandTyped(commandLine, command);
        if (commandLine == null) {
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null && !commandLine.isEmpty()) {
            commandLine = ChatHelper.controlSmsAndFlood(commandLine);
            if (commandLine != null) {
                commandLine = UIChatFrame.getInstance().formatMessageWithItemInfos(commandLine).trim();
                final UserRecruteContentMessage recruteContentMessage = new UserRecruteContentMessage();
                recruteContentMessage.setMessageContent(commandLine);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(recruteContentMessage);
                commandLine = WakfuWordsModerator.makeValidSentence(commandLine);
                if (commandLine.isEmpty()) {
                    final String errorMessage = WakfuTranslator.getInstance().getString("error.chat.operationNotPermited");
                    final ChatMessage m = new ChatMessage(errorMessage);
                    m.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(m);
                }
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
