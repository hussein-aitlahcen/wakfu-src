package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class UserChannelContentMessage extends OutputOnlyProxyMessage
{
    private String m_channelName;
    private String m_messageContent;
    
    @Override
    public byte[] encode() {
        final byte[] channel = StringUtils.toUTF8(this.m_channelName);
        byte[] content;
        try {
            content = StringUtils.toUTF8(this.m_messageContent);
        }
        catch (Exception e) {
            content = this.m_messageContent.getBytes();
        }
        final ByteBuffer bb = ByteBuffer.allocate(1 + content.length + 1 + channel.length);
        bb.put((byte)channel.length);
        bb.put(channel);
        bb.put((byte)content.length);
        bb.put(content);
        return this.addClientHeader((byte)4, bb.array());
    }
    
    @Override
    public int getId() {
        return 3151;
    }
    
    public void setChannelName(final String channelName) {
        this.m_channelName = channelName;
    }
    
    public void setMessageContent(final String messageContent) {
        this.m_messageContent = messageContent;
    }
}
