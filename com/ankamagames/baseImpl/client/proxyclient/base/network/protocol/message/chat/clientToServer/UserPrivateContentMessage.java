package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class UserPrivateContentMessage extends OutputOnlyProxyMessage
{
    private String m_userName;
    private String m_messageContent;
    
    @Override
    public byte[] encode() {
        final byte[] un = StringUtils.toUTF8(this.m_userName);
        byte[] mc;
        try {
            mc = StringUtils.toUTF8(this.m_messageContent);
        }
        catch (Exception e) {
            mc = this.m_messageContent.getBytes();
        }
        final ByteBuffer bb = ByteBuffer.allocate(1 + un.length + 1 + mc.length);
        bb.put((byte)un.length);
        bb.put(un);
        bb.put((byte)mc.length);
        bb.put(mc);
        return this.addClientHeader((byte)4, bb.array());
    }
    
    @Override
    public int getId() {
        return 3155;
    }
    
    public void setUserName(final String userName) {
        this.m_userName = userName;
    }
    
    public void setMessageContent(final String messageContent) {
        this.m_messageContent = messageContent;
    }
}
