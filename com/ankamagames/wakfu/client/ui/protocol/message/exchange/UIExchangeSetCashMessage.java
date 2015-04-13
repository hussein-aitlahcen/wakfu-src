package com.ankamagames.wakfu.client.ui.protocol.message.exchange;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UIExchangeSetCashMessage extends UIMessage
{
    private long m_exchangeId;
    private int m_cashInExchange;
    
    public long getExchangeId() {
        return this.m_exchangeId;
    }
    
    public void setExchangeId(final long exchangeId) {
        this.m_exchangeId = exchangeId;
    }
    
    public int getCashInExchange() {
        return this.m_cashInExchange;
    }
    
    public void setCashInExchange(final int cashInExchange) {
        this.m_cashInExchange = cashInExchange;
    }
}
