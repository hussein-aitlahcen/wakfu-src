package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "zonebuff", commandParameters = "&lt;add | remove &gt; &lt;buffId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Without parameter show player and zone buffs. With parameters, add or remove specified buff.", commandObsolete = false)
public class ZoneBuffCommand extends ModerationCommand
{
    public static final byte DUMP = 1;
    public static final byte ADD = 2;
    public static final byte REMOVE = 3;
    private final byte m_subCommand;
    private final int m_buffRefId;
    
    public ZoneBuffCommand(final byte subCommand) {
        super();
        this.m_subCommand = subCommand;
        this.m_buffRefId = -1;
    }
    
    public ZoneBuffCommand(final byte subCommand, final int buffRefId) {
        super();
        this.m_subCommand = subCommand;
        this.m_buffRefId = buffRefId;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)81);
        netMessage.addByteParameter(this.m_subCommand);
        netMessage.addIntParameter(this.m_buffRefId);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
