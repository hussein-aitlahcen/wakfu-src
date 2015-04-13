package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeEndMessage extends InputOnlyProxyMessage
{
    private long m_exchangeId;
    private byte m_reason;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_exchangeId = buffer.getLong();
        this.m_reason = buffer.get();
        return true;
    }
    
    @Override
    public int getId() {
        return 6050;
    }
    
    public byte getReason() {
        return this.m_reason;
    }
    
    public long getExchangeId() {
        return this.m_exchangeId;
    }
}
