package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "who", commandParameters = "&lt;pseudo&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Show account and character ID for this pseudo. who * to do for all players.", commandObsolete = false)
public class WhoCommand extends ModerationCommand
{
    private final String m_characterNamePattern;
    private final byte m_whoType;
    
    public WhoCommand(final String characterNamePattern) {
        super();
        this.m_characterNamePattern = characterNamePattern;
        this.m_whoType = 0;
    }
    
    @Override
    public boolean isValid() {
        return this.m_characterNamePattern != null && !this.m_characterNamePattern.isEmpty();
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand((short)1);
        netMessage.addByteParameter(this.m_whoType);
        netMessage.addStringParameter(this.m_characterNamePattern);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
