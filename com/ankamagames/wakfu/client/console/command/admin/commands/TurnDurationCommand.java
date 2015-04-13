package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "turnduration", commandParameters = "&lt;seconds&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Modify turn time for current fight.", commandObsolete = false)
public class TurnDurationCommand extends ModerationCommand
{
    private final int m_duration;
    
    public TurnDurationCommand(final int duration) {
        super();
        this.m_duration = duration;
    }
    
    @Override
    public boolean isValid() {
        return this.m_duration > 999 || this.m_duration == -1;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)18);
        netMessage.addIntParameter(this.m_duration);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
    
    public static String getHelp() {
        return null;
    }
}
