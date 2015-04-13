package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeInvitationMessage extends InputOnlyProxyMessage
{
    private long m_exchangeId;
    private long m_requesterId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_exchangeId = buffer.getLong();
        this.m_requesterId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 6002;
    }
    
    public long getRequesterId() {
        return this.m_requesterId;
    }
    
    public long getExchangeId() {
        return this.m_exchangeId;
    }
}
