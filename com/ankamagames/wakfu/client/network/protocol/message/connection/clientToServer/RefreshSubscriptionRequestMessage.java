package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public final class RefreshSubscriptionRequestMessage extends OutputOnlyProxyMessage
{
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)1, new byte[] { 0 });
    }
    
    @Override
    public int getId() {
        return 1203;
    }
}
