package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class PartyPrivateMessageCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        String commandLine = args.get(2);
        commandLine = ChatHelper.controlSmsAndFlood(commandLine);
        if (commandLine == null) {
            return;
        }
        commandLine = UIChatFrame.getInstance().formatMessageWithItemInfos(commandLine);
        commandLine = WakfuWordsModerator.makeValidSentence(commandLine);
        if (commandLine.length() == 0) {
            ChatHelper.pushErrorMessage("error.chat.operationNotPermited", new Object[0]);
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.getPartyComportment().isInParty()) {
            final GroupPrivateContentMessage message = new GroupPrivateContentMessage();
            message.setMessageContent(commandLine);
            message.setGroupId(localPlayer.getPartyComportment().getPartyId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        }
        else {
            final String chatMessage = WakfuTranslator.getInstance().getString("group.error.not_in_group");
            ChatManager.getInstance().pushMessage(chatMessage, 3);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
