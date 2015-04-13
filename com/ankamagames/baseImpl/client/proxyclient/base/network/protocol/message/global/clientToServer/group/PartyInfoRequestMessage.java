package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.clientToServer.group;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public final class PartyInfoRequestMessage extends OutputOnlyProxyMessage
{
    private static final byte[] DATA;
    
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)6, PartyInfoRequestMessage.DATA);
    }
    
    @Override
    public int getId() {
        return 525;
    }
    
    static {
        DATA = new byte[0];
    }
}
