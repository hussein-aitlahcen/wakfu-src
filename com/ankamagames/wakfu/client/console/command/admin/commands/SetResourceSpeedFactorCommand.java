package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "setResourceSpeedFactor | srsf", commandParameters = "&lt;speed&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Change rate of growth. Value must look like 'number.0'.", commandObsolete = false)
public final class SetResourceSpeedFactorCommand extends ModerationCommand
{
    private final float m_factor;
    
    public SetResourceSpeedFactorCommand(final float factor) {
        super();
        this.m_factor = factor;
    }
    
    @Override
    public boolean isValid() {
        return this.m_factor > 0.0f;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)86);
        netMessage.addFloatParameter(this.m_factor);
        networkEntity.sendMessage(netMessage);
    }
}
