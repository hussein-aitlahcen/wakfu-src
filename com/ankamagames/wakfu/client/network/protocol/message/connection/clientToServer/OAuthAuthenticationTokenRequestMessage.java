package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class OAuthAuthenticationTokenRequestMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        return this.addClientHeader((byte)8, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 1209;
    }
}
