package com.ankamagames.wakfu.client.ui.protocol.message.exchangeMachine;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.exchangeMachine.*;

public class UIExchangeMachineMessage extends UIMessage
{
    ExchangeEntryView m_exchangeView;
    
    public void setExchangeView(final ExchangeEntryView exchangeView) {
        this.m_exchangeView = exchangeView;
    }
    
    public ExchangeEntryView getExchangeView() {
        return this.m_exchangeView;
    }
    
    @Override
    public int getId() {
        return 19341;
    }
}
