package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.client.core.*;

public class CategoriesKey implements SOAPRequestProvider
{
    private static final String CATEGORIES_KEY_OPERATION = "CategoriesKey";
    private static final String SHOP_KEY = "sShopKey";
    private static final String LANG_PARAM_NAME = "sLang";
    private static final String KEY = "sKey";
    private final CategoriesKeyListener m_listener;
    private final String m_key;
    
    public CategoriesKey(final CategoriesKeyListener listener, final String key) {
        super();
        this.m_listener = listener;
        this.m_key = key;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement("CategoriesKey");
        final String language = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage();
        getEventOperation.putParameter("sShopKey", ShopConstants.getShopKey());
        getEventOperation.putParameter("sLang", language);
        getEventOperation.putParameter("sKey", this.m_key);
        final SOAPBody soapBody = new SOAPBody();
        soapBody.addElement(getEventOperation);
        return soapBody;
    }
    
    public CategoriesKeyListener getListener() {
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
        return "CategoriesKey{m_key='" + this.m_key + '\'' + '}';
    }
}
