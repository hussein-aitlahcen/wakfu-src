package com.ankamagames.wakfu.client.core.game.soap.shop;

import gnu.trove.*;
import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.net.soap.data.*;

public class ArticlesSearch implements SOAPRequestProvider
{
    private static final String ARTICLES_LIST = "ArticlesSearch";
    private static final String SHOP_KEY = "sShopKey";
    private static final String LANG = "sLang";
    private static final String TEXT = "sText";
    private static final String CATEGORY = "aCategory";
    private static final String PAGE = "iPage";
    private static final String SIZE = "iSize";
    private final TIntArrayList m_category;
    private final String m_text;
    private final int m_page;
    private final int m_size;
    private final ArticlesSearchListener m_listener;
    
    public ArticlesSearch(final TIntArrayList category, final int page, final String text, final int size, final ArticlesSearchListener listener) {
        super();
        this.m_category = category;
        this.m_page = page;
        this.m_text = text;
        this.m_size = size;
        this.m_listener = listener;
    }
    
    public ArticlesSearchListener getListener() {
        return this.m_listener;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement("ArticlesSearch");
        final String language = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage();
        final ArrayData category = new ArrayData();
        for (int i = 0, size = this.m_category.size(); i < size; ++i) {
            final int id = this.m_category.get(i);
            category.addValue(new IntData(id));
        }
        getEventOperation.putParameter("sShopKey", ShopConstants.getShopKey());
        getEventOperation.putParameter("sLang", language);
        getEventOperation.putParameter("sText", this.m_text);
        getEventOperation.putParameter("aCategory", category);
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
        final ArticlesSearch articlesList = (ArticlesSearch)obj;
        return this.m_page == articlesList.m_page && this.m_size == articlesList.m_size && this.m_category == articlesList.m_category;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_category.hashCode();
        result = 31 * result + this.m_text.hashCode();
        result = 31 * result + this.m_page;
        result = 31 * result + this.m_size;
        return result;
    }
    
    @Override
    public String toString() {
        return "ArticlesSearch{m_category=" + this.m_category + ", m_text='" + this.m_text + '\'' + ", m_page=" + this.m_page + ", m_size=" + this.m_size + '}';
    }
}
