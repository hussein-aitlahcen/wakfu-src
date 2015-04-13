package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class RemoveIgnoreCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final String userName = args.get(2).replaceAll("\"", "");
        final RemoveIgnoreMessage privateMessage = new RemoveIgnoreMessage();
        privateMessage.setIgnoreName(userName);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(privateMessage);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
