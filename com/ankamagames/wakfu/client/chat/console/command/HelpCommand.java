package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class HelpCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final LocalPlayerCharacter coach = WakfuGameEntity.getInstance().getLocalPlayer();
        final String helpCommand = " >" + WakfuTranslator.getInstance().getString("chat.help") + "\n";
        final ChatMessage helpMessage = new ChatMessage(helpCommand);
        helpMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(helpMessage);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
