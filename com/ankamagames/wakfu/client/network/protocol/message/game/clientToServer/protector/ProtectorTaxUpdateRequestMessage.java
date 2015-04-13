package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ProtectorTaxUpdateRequestMessage extends OutputOnlyProxyMessage
{
    private int m_protectorId;
    
    public void setProtectorId(final int protectorId) {
        this.m_protectorId = protectorId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(this.m_protectorId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15333;
    }
}
