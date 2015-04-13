package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeSetCashRequestMessage extends OutputOnlyProxyMessage
{
    private int m_cashInExchange;
    private long m_exchangeId;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 12;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putLong(this.m_exchangeId);
        buffer.putInt(this.m_cashInExchange);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        throw new UnsupportedOperationException("On tente de d\u00e9coder un message Client -> Server c\u00f4t\u00e9 client");
    }
    
    @Override
    public int getId() {
        return 6013;
    }
    
    public void setAmountOfCashInExchange(final int amountOfCash) {
        this.m_cashInExchange = amountOfCash;
    }
    
    public void setExchangeId(final long exchangeId) {
        this.m_exchangeId = exchangeId;
    }
}
