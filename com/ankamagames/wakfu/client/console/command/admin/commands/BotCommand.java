package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "bot", commandParameters = "ping &lt;accountId&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Ping specified account.", commandObsolete = false)
public class BotCommand extends ModerationCommand
{
    public static final byte PING = 1;
    private final byte m_cmd;
    private final long m_acountId;
    
    public BotCommand(final byte cmd, final long acountId) {
        super();
        this.m_cmd = cmd;
        this.m_acountId = acountId;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)1);
        netMessage.setCommand((short)190);
        netMessage.addByteParameter(this.m_cmd);
        netMessage.addLongParameter(this.m_acountId);
        networkEntity.sendMessage(netMessage);
    }
}
