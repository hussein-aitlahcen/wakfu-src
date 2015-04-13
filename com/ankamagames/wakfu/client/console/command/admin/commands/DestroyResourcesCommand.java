package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "destroyResources | dr", commandParameters = "&lt;resourceId&gt; &lt;qty&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Random destroy of specified resource in current area.", commandObsolete = false)
public class DestroyResourcesCommand extends ModerationCommand
{
    private final int m_resourceReferenceId;
    private final int m_resourcesCount;
    
    public DestroyResourcesCommand(final int resourceReferenceId) {
        super();
        this.m_resourceReferenceId = resourceReferenceId;
        this.m_resourcesCount = 1;
    }
    
    public DestroyResourcesCommand(final int resourceReferenceId, final int resourcesCount) {
        super();
        this.m_resourceReferenceId = resourceReferenceId;
        this.m_resourcesCount = resourcesCount;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)92);
        netMessage.addIntParameter(this.m_resourceReferenceId);
        netMessage.addIntParameter(this.m_resourcesCount);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
