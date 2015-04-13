package com.ankamagames.wakfu.client.ui.protocol.message.exchange;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UIReadyForExchangeRequestMessage extends UIMessage
{
    private long m_exchangeId;
    
    @Override
    public int getId() {
        return 16807;
    }
    
    public void setExchangeId(final long exchangeId) {
        this.m_exchangeId = exchangeId;
    }
    
    public long getExchangeId() {
        return this.m_exchangeId;
    }
}
