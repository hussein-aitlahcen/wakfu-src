package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.client.core.*;

public class ArticlesList implements SOAPRequestProvider
{
    private static final String ARTICLES_LIST = "ArticlesList";
    private static final String GONDOLA_HEAD_LIST = "ArticlesGondolahead";
    private static final String SHOP_KEY = "sShopKey";
    private static final String LANG = "sLang";
    private static final String CATEGORY = "iCategory";
    private static final String GONDOLA_HEAD = "iGondolaheadId";
    private static final String PAGE = "iPage";
    private static final String SIZE = "iSize";
    private final int m_category;
    private final int m_page;
    private final int m_size;
    private final ArticlesListListener m_listener;
    private final boolean m_gondolaHead;
    
    public ArticlesList(final int category, final int page, final int size, final ArticlesListListener listener) {
        this(category, page, size, listener, false);
    }
    
    public ArticlesList(final int category, final int page, final int size, final ArticlesListListener listener, final boolean gondolaHead) {
        super();
        this.m_category = category;
        this.m_page = page;
        this.m_size = size;
        this.m_listener = listener;
        this.m_gondolaHead = gondolaHead;
    }
    
    public ArticlesListListener getListener() {
        return this.m_listener;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement(this.m_gondolaHead ? "ArticlesGondolahead" : "ArticlesList");
        final String language = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage();
        getEventOperation.putParameter("sShopKey", ShopConstants.getShopKey());
        getEventOperation.putParameter("sLang", language);
        getEventOperation.putParameter(this.m_gondolaHead ? "iGondolaheadId" : "iCategory", this.m_category);
        getEventOperation.putParameter("iPage", this.m_page);
        getEventOperation.putParameter("iSize", this.m_size);
        final SOAPBody soapBody = new SOAPBody();
        soapBody.addElement(getEventOperation);
        return soapBody;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final ArticlesList articlesList = (ArticlesList)obj;
        return this.m_page == articlesList.m_page && this.m_size == articlesList.m_size && this.m_category == articlesList.m_category;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_category;
        result = 31 * result + this.m_page;
        result = 31 * result + this.m_size;
        return result;
    }
    
    @Override
    public String toString() {
        return "ArticlesList{m_category=" + this.m_category + ", m_page=" + this.m_page + ", m_size=" + this.m_size + '}';
    }
}
