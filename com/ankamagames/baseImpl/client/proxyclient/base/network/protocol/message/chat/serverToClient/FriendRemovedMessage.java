package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class FriendRemovedMessage extends InputOnlyProxyMessage
{
    private String m_friendName;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] cn = new byte[bb.get() & 0xFF];
        bb.get(cn);
        this.m_friendName = StringUtils.fromUTF8(cn);
        return true;
    }
    
    public String getFriendName() {
        return this.m_friendName;
    }
    
    @Override
    public int getId() {
        return 3160;
    }
}
