package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "freeaccess", commandParameters = "&lt;on | off&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Enable or disable subscription limits", commandObsolete = false)
public class FreeAccess extends ModerationCommand
{
    private static final byte ACTION_READ = 0;
    private static final byte ACTION_SET = 1;
    private static final byte ACTION_UNSET = 2;
    private final byte m_action;
    
    public FreeAccess() {
        super();
        this.m_action = 0;
    }
    
    public FreeAccess(final boolean set) {
        super();
        this.m_action = (byte)(set ? 1 : 2);
    }
    
    public FreeAccess(final long accountId, final boolean set) {
        super();
        this.m_action = (byte)(set ? 1 : 2);
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand((short)139);
        netMessage.addByteParameter(this.m_action);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
