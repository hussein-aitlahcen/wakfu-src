package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class UserPoliticContentMessage extends OutputOnlyProxyMessage
{
    private String m_messageContent;
    
    @Override
    public byte[] encode() {
        final byte[] mc = StringUtils.toUTF8(this.m_messageContent);
        final ByteBuffer bb = ByteBuffer.allocate(1 + mc.length);
        bb.put((byte)mc.length);
        bb.put(mc);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 3163;
    }
    
    public void setMessageContent(final String messageContent) {
        this.m_messageContent = messageContent;
    }
}
