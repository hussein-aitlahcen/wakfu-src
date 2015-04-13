package com.ankamagames.wakfu.client.network.protocol.frame.guild;

import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.messagebox.*;

class GuildInvitationMessageRunner implements MessageRunner<GuildInvitationMessage>
{
    @Override
    public boolean run(final GuildInvitationMessage msg) {
        final String inviterName = msg.getInviterName();
        final String guildName = msg.getGuildName();
        final String invitMessage = WakfuTranslator.getInstance().getString("group.guild.invitation", inviterName, guildName);
        final String messageBoxIconUrl = WakfuMessageBoxConstants.getMessageBoxIconUrl(4);
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(invitMessage, messageBoxIconUrl, 2073L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new GuildInvitationResultMessage(type == 8));
            }
        });
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 20056;
    }
}
