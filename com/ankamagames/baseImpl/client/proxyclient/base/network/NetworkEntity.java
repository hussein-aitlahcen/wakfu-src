package com.ankamagames.baseImpl.client.proxyclient.base.network;

import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.google.common.base.*;
import io.netty.channel.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;

public abstract class NetworkEntity extends FrameworkEntity
{
    @Override
    public Optional<ChannelFuture> sendMessage(final Message message, final boolean batch) {
        try {
            if (message.getId() == 3153) {
                final UserVicinityContentMessage vicinityMessage = (UserVicinityContentMessage)message;
                final String chatMessage = vicinityMessage.getMessageContent();
                if (chatMessage != null && chatMessage.contains("kama")) {
                    NetworkEntity.m_logger.info((Object)("vicinity sent=" + chatMessage), (Throwable)new Exception());
                }
            }
        }
        catch (Exception e) {
            NetworkEntity.m_logger.error((Object)"erreur lors du debug des vicinity", (Throwable)e);
        }
        return super.sendMessage(message, batch);
    }
}
