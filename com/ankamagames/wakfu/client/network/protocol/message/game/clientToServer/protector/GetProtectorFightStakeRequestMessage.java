package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class GetProtectorFightStakeRequestMessage extends OutputOnlyProxyMessage
{
    private final int m_protectorId;
    
    public GetProtectorFightStakeRequestMessage(final int protectorId) {
        super();
        this.m_protectorId = protectorId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_protectorId);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 15316;
    }
}
