package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.chat.*;

public class ClearChatCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final ChatViewManager currentWindow = ChatWindowManager.getInstance().getCurrentWindow();
        if (currentWindow == null) {
            return;
        }
        final ChatView chatView = currentWindow.getCurrentView();
        if (chatView == null) {
            return;
        }
        chatView.clear();
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
