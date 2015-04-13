package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "resetAccountMarketEntries | rame ", commandParameters = "&lt;accountId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Set all market entries of this account ID as outdated", commandObsolete = false)
public class ResetAccountMarketEntriesCommand extends ModerationCommand
{
    private final long m_accountId;
    
    public ResetAccountMarketEntriesCommand(final long accountId) {
        super();
        this.m_accountId = accountId;
    }
    
    @Override
    public boolean isValid() {
        return this.m_accountId > 0L;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)235);
        msg.addLongParameter(this.m_accountId);
        networkEntity.sendMessage(msg);
    }
}
