package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeInvitationAnswerMessage extends OutputOnlyProxyMessage
{
    private long m_exchangeId;
    private byte m_exchangeInvitationResult;
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.putLong(this.m_exchangeId);
        buffer.put(this.m_exchangeInvitationResult);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 6003;
    }
    
    public void setInvitationResult(final byte exchangeInvitationResult) {
        this.m_exchangeInvitationResult = exchangeInvitationResult;
    }
    
    public void setExchangeId(final long exchangeId) {
        this.m_exchangeId = exchangeId;
    }
}
