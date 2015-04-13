package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "redMessage | rm ", commandParameters = "&lt;\"message\"&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Send a red message to all people in a local proximity. It bypass all channels", commandObsolete = false)
public class RedMessageCommand extends ModerationCommand
{
    private String m_message;
    
    public RedMessageCommand(final String message) {
        super();
        this.m_message = message;
    }
    
    @Override
    public boolean isValid() {
        return !this.m_message.isEmpty();
    }
    
    @Override
    public void execute() {
        if (this.m_message.isEmpty() || this.m_message.contains("missing ESCAPED_STRING")) {
            ConsoleManager.getInstance().err("Message is too short.");
            return;
        }
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        if (this.m_message.indexOf(34) == 0) {
            this.m_message = this.m_message.substring(1);
        }
        if (this.m_message.indexOf(34) == this.m_message.length() - 1) {
            this.m_message = this.m_message.substring(0, this.m_message.length() - 1);
        }
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)196);
        msg.addStringParameter(this.m_message);
        networkEntity.sendMessage(msg);
    }
}
