package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "serverlock | sl", commandParameters = "&lt;on | off&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Lock server for non admin accounts.", commandObsolete = false)
public class ServerlockCommand extends ModerationCommand
{
    private byte m_lock;
    
    public ServerlockCommand(final byte lock) {
        super();
        this.m_lock = lock;
    }
    
    @Override
    public boolean isValid() {
        return this.m_lock == 0 || this.m_lock == 1 || this.m_lock == 2;
    }
    
    @Override
    public void execute() {
        this.sendTo((byte)1);
        this.sendTo((byte)2);
    }
    
    private void sendTo(final byte serveridConnection) {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId(serveridConnection);
        netMessage.setCommand((short)4);
        netMessage.addByteParameter(this.m_lock);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
