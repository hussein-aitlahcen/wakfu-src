package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "instanceusage", commandParameters = "&lt;instanceId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Show list of people present in this instance.", commandObsolete = false)
public class InstanceUsageCommand extends ModerationCommand
{
    private final short m_instanceId;
    
    public InstanceUsageCommand(final short instanceId) {
        super();
        this.m_instanceId = instanceId;
    }
    
    @Override
    public boolean isValid() {
        return this.m_instanceId >= 0;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)37);
        netMessage.addShortParameter(this.m_instanceId);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
