package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.client.core.*;

public class Home implements SOAPRequestProvider
{
    private static final String HOME_OPERATION = "Home";
    private static final String SHOP_KEY = "sShopKey";
    private static final String LANG_PARAM_NAME = "sLang";
    private final HomeListener m_listener;
    
    public Home(final HomeListener listener) {
        super();
        this.m_listener = listener;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement("Home");
        final String language = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage();
        getEventOperation.putParameter("sShopKey", ShopConstants.getShopKey());
        getEventOperation.putParameter("sLang", language);
        final SOAPBody soapBody = new SOAPBody();
        soapBody.addElement(getEventOperation);
        return soapBody;
    }
    
    public HomeListener getListener() {
        return this.m_listener;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj != null && this.getClass() == obj.getClass());
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public String toString() {
        return "Home{}";
    }
}
