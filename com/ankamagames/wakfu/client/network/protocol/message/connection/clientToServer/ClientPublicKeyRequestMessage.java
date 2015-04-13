package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class ClientPublicKeyRequestMessage extends OutputOnlyProxyMessage
{
    private final byte m_serverId;
    
    public ClientPublicKeyRequestMessage(final byte serverId) {
        super();
        this.m_serverId = serverId;
    }
    
    @Override
    public byte[] encode() {
        return this.addClientHeader(this.m_serverId, PrimitiveArrays.EMPTY_BYTE_ARRAY);
    }
    
    @Override
    public int getId() {
        return 1033;
    }
}
