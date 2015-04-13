package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class PrivateContentCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final String dest = args.get(2).replaceAll("\"", "");
        String commandLine = args.get(3);
        commandLine = UIChatFrame.getInstance().formatMessageWithItemInfos(commandLine);
        if (dest.contains("<")) {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.chat.privateFieldEmpty"), 3);
            return;
        }
        final LocalPlayerCharacter localPlayerCharacter = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayerCharacter != null) {
            ChatHelper.sendPrivateMessage(commandLine, localPlayerCharacter.getName(), localPlayerCharacter.getId(), dest);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
