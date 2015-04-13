package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "ban | unban", commandParameters = " &lt;accountId&gt; &lt;time&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Ban or unban the player [for specified time if 'ban' used]. Don't use time param for unban.", commandObsolete = false)
public class BanCommand extends ModerationCommand
{
    private final long m_clientId;
    private final boolean m_setBan;
    private final int m_minutes;
    
    public BanCommand(final long clientId, final boolean setBan, final int minutes) {
        super();
        this.m_clientId = clientId;
        this.m_setBan = setBan;
        this.m_minutes = minutes;
    }
    
    @Override
    public boolean isValid() {
        return this.m_clientId > 0L && (!this.m_setBan || this.m_minutes > 0);
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)1);
        netMessage.setCommand((short)49);
        netMessage.addLongParameter(this.m_clientId);
        netMessage.addBooleanParameter(this.m_setBan);
        netMessage.addIntParameter(this.m_minutes);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
