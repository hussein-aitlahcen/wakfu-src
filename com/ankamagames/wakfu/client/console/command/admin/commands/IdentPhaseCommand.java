package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "identphase", commandParameters = "&lt;accountId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Give information about connection state of this account and his rights.", commandObsolete = false)
public class IdentPhaseCommand extends ModerationCommand
{
    private final long m_userIdToCheck;
    
    public IdentPhaseCommand(final long userId) {
        super();
        this.m_userIdToCheck = userId;
    }
    
    public long getUserIdToCheck() {
        return this.m_userIdToCheck;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)1);
        netMessage.setCommand((short)47);
        netMessage.addLongParameter(this.m_userIdToCheck);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
