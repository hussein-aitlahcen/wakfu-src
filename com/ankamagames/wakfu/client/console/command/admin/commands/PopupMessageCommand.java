package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "popupMessage | popupm | pumessage | pum", commandParameters = "&lt;pseudo&gt; &lt;\"message\"&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Send a modal popup to player, with the message. Use it to ask to player to contact you so quickly, under penalty of kick.", commandObsolete = false)
public class PopupMessageCommand extends ModerationCommand
{
    private final String m_characterNamePattern;
    private String m_message;
    
    public PopupMessageCommand(final String characterNamePattern, final String message) {
        super();
        this.m_characterNamePattern = characterNamePattern;
        this.m_message = message;
    }
    
    @Override
    public boolean isValid() {
        return this.m_characterNamePattern != null && !this.m_characterNamePattern.isEmpty();
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00c3©der \u00c3  ces commandes il faut \u00c3ªtre connect\u00c3© !");
            return;
        }
        this.m_message = this.m_message.substring(1, this.m_message.length() - 1);
        if (this.m_message.isEmpty() || this.m_message.contains("ESCAPED_STRING")) {
            ConsoleManager.getInstance().err("Message is too short");
            return;
        }
        if (this.m_message.length() > 255) {
            ConsoleManager.getInstance().err("Message is too long.");
            return;
        }
        desactivateDoNotDisturb(networkEntity);
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)2);
        msg.setCommand((short)195);
        msg.addStringParameter(this.m_message);
        msg.addStringParameter(this.m_characterNamePattern);
        networkEntity.sendMessage(msg);
    }
    
    private static void desactivateDoNotDisturb(final NetworkEntity networkEntity) {
        final ModerationCommandMessage worldMessage = new ModerationCommandMessage();
        worldMessage.setServerId((byte)2);
        worldMessage.setCommand((short)184);
        worldMessage.addByteParameter(Byte.parseByte("0"));
        networkEntity.sendMessage(worldMessage);
    }
}
