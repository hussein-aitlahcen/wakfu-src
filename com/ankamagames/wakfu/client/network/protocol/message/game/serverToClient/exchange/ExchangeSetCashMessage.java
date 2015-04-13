package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.exchange;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ExchangeSetCashMessage extends InputOnlyProxyMessage
{
    private long m_exchangeId;
    private long m_userId;
    private byte m_validity;
    private int m_amountOfCash;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_exchangeId = buffer.getLong();
        this.m_userId = buffer.getLong();
        this.m_validity = buffer.get();
        this.m_amountOfCash = buffer.getInt();
        return true;
    }
    
    public long getExchangeId() {
        return this.m_exchangeId;
    }
    
    public long getUserId() {
        return this.m_userId;
    }
    
    public byte getValidity() {
        return this.m_validity;
    }
    
    public int getAmountOfCash() {
        return this.m_amountOfCash;
    }
    
    @Override
    public int getId() {
        return 6014;
    }
}
