package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "sysmsg", commandParameters = "&lt;\"Message\"&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Send to all connected players a message.", commandObsolete = false)
public class SysmsgCommand extends ModerationCommand
{
    private final String m_targets;
    private final String m_message;
    private static final String ALL_SYMBOL = "*";
    
    public SysmsgCommand(final String message) {
        this("*", message);
    }
    
    public SysmsgCommand(final String targets, final String message) {
        super();
        this.m_targets = targets;
        this.m_message = message;
    }
    
    @Override
    public boolean isValid() {
        return this.m_message != null && this.m_targets != null;
    }
    
    @Override
    public void execute() {
        this.sendCommand();
    }
    
    private void sendCommand() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand((short)34);
        netMessage.addStringParameter(this.m_targets);
        netMessage.addStringParameter(this.m_message);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
