package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class AddIgnoreMessage extends OutputOnlyProxyMessage
{
    private String m_ignoreName;
    
    @Override
    public byte[] encode() {
        byte[] in = new byte[0];
        in = StringUtils.toUTF8(this.m_ignoreName);
        final ByteBuffer bb = ByteBuffer.allocate(1 + in.length);
        bb.put((byte)in.length);
        bb.put(in);
        return this.addClientHeader((byte)4, bb.array());
    }
    
    @Override
    public int getId() {
        return 3131;
    }
    
    public void setIgnoreName(final String ignoreName) {
        this.m_ignoreName = ignoreName;
    }
}
