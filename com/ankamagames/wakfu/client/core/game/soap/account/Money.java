package com.ankamagames.wakfu.client.core.game.soap.account;

import com.ankamagames.framework.net.soap.*;

public class Money implements SOAPRequestProvider
{
    private static final String MONEY = "Money";
    private final MoneyListener m_listener;
    
    public Money(final MoneyListener listener) {
        super();
        this.m_listener = listener;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement("Money");
        final SOAPBody soapBody = new SOAPBody();
        soapBody.addElement(getEventOperation);
        return soapBody;
    }
    
    public MoneyListener getListener() {
        return this.m_listener;
    }
    
    @Override
    public String toString() {
        return "Money{}";
    }
}
