package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class ChatCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (!ModalManager.getInstance().isEmpty()) {
            return;
        }
        focusCurrentChatWindow();
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    public static void focusCurrentChatWindow() {
        FocusManager.getInstance().setFocused((Widget)UIChatFrame.getInstance().getCurrentChatWindow().getElementMap().getElement("textEditorRenderableContainer.chatInput"));
    }
}
