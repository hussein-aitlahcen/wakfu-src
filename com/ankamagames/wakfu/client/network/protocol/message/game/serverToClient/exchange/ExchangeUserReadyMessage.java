package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeUserReadyMessage extends InputOnlyProxyMessage
{
    private long m_exchangeId;
    private long m_userId;
    private boolean m_ready;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_exchangeId = buffer.getLong();
        this.m_userId = buffer.getLong();
        this.m_ready = (buffer.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 6022;
    }
    
    public long getExchangeId() {
        return this.m_exchangeId;
    }
    
    public long getUserId() {
        return this.m_userId;
    }
    
    public boolean isReady() {
        return this.m_ready;
    }
}
