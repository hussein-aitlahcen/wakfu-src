package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeInvitationConfirmationMessage extends InputOnlyProxyMessage
{
    private long m_exchangeId;
    private byte m_result;
    private long m_requestedId;
    private long m_answererId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_exchangeId = buffer.getLong();
        this.m_result = buffer.get();
        this.m_requestedId = buffer.getLong();
        this.m_answererId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 6004;
    }
    
    public long getExchangeId() {
        return this.m_exchangeId;
    }
    
    public long getRequestedId() {
        return this.m_requestedId;
    }
    
    public byte getResult() {
        return this.m_result;
    }
    
    public long getAnswererId() {
        return this.m_answererId;
    }
}
