package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "banrequest", commandParameters = "&lt;accountId&gt; &lt;characterId&gt; &lt;reason&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Send a ban request to support.", commandObsolete = false)
public class BanRequestCommand extends ModerationCommand
{
    private final long m_clientId;
    private long m_characterId;
    private final String m_reason;
    
    public BanRequestCommand(final long clientId, final long characterId, final String reason) {
        super();
        this.m_characterId = Long.MIN_VALUE;
        this.m_clientId = clientId;
        this.m_characterId = characterId;
        this.m_reason = reason;
    }
    
    @Override
    public boolean isValid() {
        return this.m_clientId > 0L && this.m_reason != null && this.m_characterId > Long.MIN_VALUE;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)1);
        netMessage.setCommand((short)105);
        netMessage.addLongParameter(this.m_clientId);
        netMessage.addLongParameter(this.m_characterId);
        netMessage.addStringParameter(this.m_reason);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
