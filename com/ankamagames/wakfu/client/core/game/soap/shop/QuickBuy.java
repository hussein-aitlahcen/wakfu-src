package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.client.core.*;

public class QuickBuy implements SOAPRequestProvider
{
    private static final String QUICK_BUY_OP = "QuickBuy";
    private static final String SHOP_KEY = "sShopKey";
    private static final String LANG = "sLang";
    private static final String ARTICLE_ID = "iArticleId";
    private static final String QUANTITY = "iQuantity";
    private final int m_articleId;
    private final int m_quantity;
    private final QuickBuyListener m_listener;
    
    public QuickBuy(final int articleId, final int quantity, final QuickBuyListener listener) {
        super();
        this.m_articleId = articleId;
        this.m_quantity = quantity;
        this.m_listener = listener;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement("QuickBuy");
        final String language = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage();
        getEventOperation.putParameter("sShopKey", ShopConstants.getShopKey());
        getEventOperation.putParameter("sLang", language);
        getEventOperation.putParameter("iArticleId", this.m_articleId);
        getEventOperation.putParameter("iQuantity", this.m_quantity);
        final SOAPBody soapBody = new SOAPBody();
        soapBody.addElement(getEventOperation);
        return soapBody;
    }
    
    public QuickBuyListener getListener() {
        return this.m_listener;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final QuickBuy quickBuy = (QuickBuy)obj;
        return this.m_articleId == quickBuy.m_articleId && this.m_quantity == quickBuy.m_quantity;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_articleId;
        result = 31 * result + this.m_quantity;
        return result;
    }
    
    @Override
    public String toString() {
        return "QuickBuy{m_articleId=" + this.m_articleId + ", m_quantity=" + this.m_quantity + '}';
    }
}
