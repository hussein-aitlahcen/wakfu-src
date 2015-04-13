package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class AddFriendMessage extends OutputOnlyProxyMessage
{
    private String m_friendName;
    
    @Override
    public byte[] encode() {
        final byte[] fn = StringUtils.toUTF8(this.m_friendName);
        final ByteBuffer bb = ByteBuffer.allocate(1 + fn.length);
        bb.put((byte)fn.length);
        bb.put(fn);
        return this.addClientHeader((byte)4, bb.array());
    }
    
    @Override
    public int getId() {
        return 3129;
    }
    
    public void setFriendName(final String friendName) {
        this.m_friendName = friendName;
    }
}
