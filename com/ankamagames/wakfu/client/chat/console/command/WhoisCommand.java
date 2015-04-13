package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public final class WhoisCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final String userName = args.get(2).replaceAll("\"", "");
        final WhoisRequestMessage whoisMessage = new WhoisRequestMessage();
        whoisMessage.setCharacterName(userName);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(whoisMessage);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
