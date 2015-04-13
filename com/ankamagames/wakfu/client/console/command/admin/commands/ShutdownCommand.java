package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "shutdown", commandParameters = "&lt;timeFrame | stop | nothing&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Stop all the servers. TimeFrame = 0 = stop a shutdown, timeFrame = -1 = show shutdown state.", commandObsolete = false)
public class ShutdownCommand extends ModerationCommand
{
    private int m_delay;
    
    public ShutdownCommand(final int delay) {
        super();
        this.m_delay = delay;
    }
    
    @Override
    public boolean isValid() {
        return this.m_delay >= -1;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand((short)9);
        netMessage.addIntParameter(this.m_delay);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
