package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient.errorMessage;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class UserNotFoundMessage extends InputOnlyProxyMessage
{
    private String m_userName;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte[] un = new byte[bb.get()];
        bb.get(un);
        this.m_userName = StringUtils.fromUTF8(un);
        return true;
    }
    
    @Override
    public int getId() {
        return 3204;
    }
    
    public String getUserName() {
        return this.m_userName;
    }
}
