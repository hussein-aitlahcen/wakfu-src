package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class ModeratorHelpRequestMessage extends OutputOnlyProxyMessage
{
    private String m_messageBase;
    
    public ModeratorHelpRequestMessage(final String msg) {
        super();
        this.m_messageBase = msg;
    }
    
    @Override
    public byte[] encode() {
        final String message = this.m_messageBase;
        final byte[] content = StringUtils.toUTF8(message);
        final ByteBuffer buffer = ByteBuffer.allocate(1 + content.length);
        buffer.put((byte)content.length);
        buffer.put(content);
        return this.addClientHeader((byte)4, buffer.array());
    }
    
    @Override
    public int getId() {
        return 3169;
    }
}
