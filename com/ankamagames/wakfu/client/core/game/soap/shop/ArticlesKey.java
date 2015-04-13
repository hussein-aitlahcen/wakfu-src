package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.client.core.*;

public class ArticlesKey implements SOAPRequestProvider
{
    private static final String ARTICLES_KEY = "ArticlesKey";
    private static final String SHOP_KEY = "sShopKey";
    private static final String LANG = "sLang";
    private static final String KEY = "sKey";
    private final String m_key;
    private final ArticlesKeyListener m_listener;
    
    public ArticlesKey(final ArticlesKeyListener listener, final String key) {
        super();
        this.m_listener = listener;
        this.m_key = key;
    }
    
    public ArticlesKeyListener getListener() {
        return this.m_listener;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement("ArticlesKey");
        final String language = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage();
        getEventOperation.putParameter("sShopKey", ShopConstants.getShopKey());
        getEventOperation.putParameter("sLang", language);
        getEventOperation.putParameter("sKey", this.m_key);
        final SOAPBody soapBody = new SOAPBody();
        soapBody.addElement(getEventOperation);
        return soapBody;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ArticlesKey that = (ArticlesKey)o;
        return this.m_key.equals(that.m_key);
    }
    
    @Override
    public int hashCode() {
        return this.m_key.hashCode();
    }
    
    @Override
    public String toString() {
        return "ArticlesKey{m_key='" + this.m_key + '\'' + '}';
    }
}
