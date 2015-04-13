package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeSetReadyMessage extends OutputOnlyProxyMessage
{
    private long m_exchangeId;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(this.m_exchangeId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 6021;
    }
    
    public void setExchangeId(final long exchangeId) {
        this.m_exchangeId = exchangeId;
    }
}
