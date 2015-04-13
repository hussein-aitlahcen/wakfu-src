package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeCancelMessage extends OutputOnlyProxyMessage
{
    private long m_exchangeId;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 8;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putLong(this.m_exchangeId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 6049;
    }
    
    public void setExchangeId(final long exchangeId) {
        this.m_exchangeId = exchangeId;
    }
}
