package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.mailbox;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class SendMailMessage extends OutputOnlyProxyMessage
{
    private String m_receiverName;
    private String m_title;
    private String m_message;
    
    @Override
    public byte[] encode() {
        final byte[] tName = StringUtils.toUTF8(this.m_receiverName);
        final byte[] tTitle = StringUtils.toUTF8(this.m_title);
        final byte[] tMessage = StringUtils.toUTF8(this.m_message);
        final int nameSize = tName.length;
        final int titleSize = tTitle.length;
        final int messageSize = tMessage.length;
        final ByteBuffer bb = ByteBuffer.allocate(4 + nameSize + 4 + titleSize + 4 + messageSize);
        bb.putInt(nameSize);
        bb.put(tName);
        bb.putInt(titleSize);
        bb.put(tTitle);
        bb.putInt(messageSize);
        bb.put(tMessage);
        return this.addClientHeader((byte)6, bb.array());
    }
    
    @Override
    public int getId() {
        return 539;
    }
    
    public void setReceiverName(final String receiverName) {
        this.m_receiverName = receiverName;
    }
    
    public void setTitle(final String title) {
        this.m_title = title;
    }
    
    public void setMessage(final String message) {
        this.m_message = message;
    }
}
