package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.social.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class AwayCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final PlayerAFKNotificationMessage afkMessage = new PlayerAFKNotificationMessage();
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(afkMessage);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
