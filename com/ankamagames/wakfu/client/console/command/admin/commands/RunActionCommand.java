package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "runaction", commandParameters = "&lt;actionId&gt; &lt;scenarioId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Execute specified action and scenario.", commandObsolete = false)
public class RunActionCommand extends ModerationCommand
{
    private final int m_actionId;
    private final int m_scenarioId;
    
    public RunActionCommand(final int actionId, final int scenarioId) {
        super();
        this.m_scenarioId = scenarioId;
        this.m_actionId = actionId;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)19);
        netMessage.addIntParameter(this.m_actionId);
        netMessage.addIntParameter(this.m_scenarioId);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
