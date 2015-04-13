package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "listLoot | ll", commandParameters = "&lt;monsterId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Show loot table of specified monster.", commandObsolete = false)
public class ListLootCommand extends ModerationCommand
{
    private final short m_monsterId;
    
    public ListLootCommand(final short s) {
        super();
        this.m_monsterId = s;
    }
    
    @Override
    public boolean isValid() {
        return this.m_monsterId > 0;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.addShortParameter(this.m_monsterId);
        netMessage.setCommand((short)204);
        networkEntity.sendMessage(netMessage);
    }
}
