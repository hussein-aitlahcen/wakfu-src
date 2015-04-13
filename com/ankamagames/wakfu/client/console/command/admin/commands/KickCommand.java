package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "kick", commandParameters = "&lt;pseudo&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Kick the player.", commandObsolete = false)
public class KickCommand extends ModerationCommand
{
    private final String m_characterNamePattern;
    private final String m_reason;
    
    public KickCommand(final String characterNamePattern) {
        this(characterNamePattern, "");
    }
    
    public KickCommand(final String characterNamePattern, final String reason) {
        super();
        this.m_characterNamePattern = characterNamePattern;
        this.m_reason = reason;
    }
    
    @Override
    public boolean isValid() {
        return this.m_characterNamePattern != null;
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            final ModerationCommandMessage netMessage = new ModerationCommandMessage();
            netMessage.setServerId((byte)2);
            netMessage.setCommand((short)7);
            netMessage.addStringParameter(this.m_characterNamePattern);
            netMessage.addStringParameter(this.m_reason);
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
