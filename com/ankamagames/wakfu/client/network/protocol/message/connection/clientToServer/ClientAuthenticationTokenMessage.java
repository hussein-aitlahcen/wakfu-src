package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;

public class ClientAuthenticationTokenMessage extends OutputOnlyProxyMessage
{
    private String m_token;
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        final byte[] utfToken = StringUtils.toUTF8(this.m_token);
        ba.putInt(utfToken.length);
        ba.put(utfToken);
        return this.addClientHeader((byte)1, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 1213;
    }
    
    public void setToken(final String token) {
        this.m_token = token;
    }
}
