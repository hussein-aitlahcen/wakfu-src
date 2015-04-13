package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.auth;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class AuthentificationTokenResultMessage extends InputOnlyProxyMessage
{
    private String m_token;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final int tokenSize = buffer.getInt();
        final byte[] bytes = new byte[tokenSize];
        buffer.get(bytes);
        this.m_token = StringUtils.fromUTF8(bytes);
        return true;
    }
    
    @Override
    public int getId() {
        return 2079;
    }
    
    public String getToken() {
        return this.m_token;
    }
    
    @Override
    public String toString() {
        return "AuthentificationTokenResultMessage{m_token='" + this.m_token + '\'' + '}';
    }
}
