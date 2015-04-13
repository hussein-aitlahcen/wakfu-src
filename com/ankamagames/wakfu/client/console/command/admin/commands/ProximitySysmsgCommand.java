package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "sysmsg", commandParameters = "&lt;%radius&gt; &lt;\"message\"&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Local sysmsg of specified radius.", commandObsolete = false)
public class ProximitySysmsgCommand extends ModerationCommand
{
    private final int m_radius;
    private final String m_message;
    
    public ProximitySysmsgCommand(final int radius, final String message) {
        super();
        this.m_radius = radius;
        this.m_message = message;
    }
    
    @Override
    public boolean isValid() {
        return this.m_message != null && this.m_radius >= 0;
    }
    
    @Override
    public void execute() {
        final int nbPartitions = (2 * this.m_radius + 1) * (2 * this.m_radius + 1);
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox("<b>Attention !\n\nVous \u00eates sur le point d'envoyer un message \u00e0 tous des joueurs se trouvant sur un total d'environ " + nbPartitions + " partitions, \u00eates vous s\u00fbr ?\n\nMessage :</b>\n" + this.m_message, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        if (messageBoxControler != null) {
            messageBoxControler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        ProximitySysmsgCommand.this.sendCommand();
                    }
                }
            });
        }
    }
    
    void sendCommand() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)54);
        netMessage.addIntParameter(this.m_radius);
        netMessage.addStringParameter(this.m_message);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
