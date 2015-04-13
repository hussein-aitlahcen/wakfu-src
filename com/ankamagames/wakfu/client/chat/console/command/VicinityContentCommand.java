package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class VicinityContentCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        String commandLine = this.getFormattedCommand(args.get(0).trim());
        if (commandLine == null) {
            return;
        }
        if (commandLine.startsWith("/")) {
            return;
        }
        final UserVicinityContentMessage vicinityMessage = new UserVicinityContentMessage();
        vicinityMessage.setMessageContent(commandLine);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(vicinityMessage);
        commandLine = WakfuWordsModerator.makeValidSentence(commandLine);
        if (commandLine.length() == 0) {
            ChatHelper.pushErrorMessage("error.chat.operationNotPermited", new Object[0]);
        }
    }
    
    private String getFormattedCommand(final String line) {
        final String command = ChatConfigurator.getCommandForChannelName(ChatCommandsParameters.VICINITY.getCommandName()).getCommand();
        String commandLine = ChatHelper.checkForCommandTyped(line, command);
        if (commandLine == null || commandLine.length() == 0) {
            return null;
        }
        commandLine = ChatHelper.controlSmsAndFlood(commandLine);
        if (commandLine == null) {
            return null;
        }
        return UIChatFrame.getInstance().formatMessageWithItemInfos(commandLine).trim();
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
