package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class AdminChannelMessageCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final String commandLine = args.get(2);
        if (!AdminRightHelper.checkRights(WakfuGameEntity.getInstance().getLocalPlayer().getAccountInformationHandler().getAdminRights(), AdminRightHelper.NO_RIGHT)) {
            final UserChannelContentMessage message = new UserChannelContentMessage();
            message.setMessageContent(commandLine);
            message.setChannelName("admin_channel");
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
