package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "sysmsg", commandParameters = "&lt;\"message\"&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "Send a system message to all players. Do not abuse, it's a so heavy command.", commandObsolete = false)
public class MsgAllCommand extends ModerationCommand
{
    private final String m_message;
    
    public MsgAllCommand(final String message) {
        super();
        this.m_message = message;
    }
    
    @Override
    public boolean isValid() {
        return this.m_message != null && !this.m_message.isEmpty();
    }
    
    @Override
    public void execute() {
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox("<b>Attention !\n\nVous \u00eates sur le point d'envoyer un message \u00e0 l'ensemble des joueurs connect\u00e9s, \u00eates vous s\u00fbr ?\n\nMessage :</b>\n" + this.m_message, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    MsgAllCommand.this.sendCommand();
                }
            }
        });
    }
    
    void sendCommand() {
        final UserChannelContentMessage message = new UserChannelContentMessage();
        message.setMessageContent(this.m_message);
        message.setChannelName("all_channel");
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(message);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
