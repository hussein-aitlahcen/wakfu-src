package com.ankamagames.wakfu.client.console.command.common;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.message.chat.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class BindCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final UIChatContentMessage contentMessage = new UIChatContentMessage();
        contentMessage.setMessage(args.get(2));
        Worker.getInstance().pushMessage(contentMessage);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
