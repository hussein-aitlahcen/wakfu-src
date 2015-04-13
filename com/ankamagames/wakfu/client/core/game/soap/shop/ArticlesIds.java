package com.ankamagames.wakfu.client.core.game.soap.shop;

import gnu.trove.*;
import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.net.soap.data.*;

public class ArticlesIds implements SOAPRequestProvider
{
    private static final String ARTICLES_LIST = "ArticlesIds";
    private static final String SHOP_KEY = "sShopKey";
    private static final String LANG = "sLang";
    private static final String IDS = "aIds";
    private final TIntArrayList m_ids;
    private final ArticlesIdsListener m_listener;
    
    public ArticlesIds(final TIntArrayList ids, final ArticlesIdsListener listener) {
        super();
        this.m_ids = ids;
        this.m_listener = listener;
    }
    
    public ArticlesIdsListener getListener() {
        return this.m_listener;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement("ArticlesIds");
        final String language = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage();
        final ArrayData ids = new ArrayData();
        for (int i = 0, size = this.m_ids.size(); i < size; ++i) {
            final int id = this.m_ids.get(i);
            ids.addValue(new IntData(id));
        }
        getEventOperation.putParameter("sShopKey", ShopConstants.getShopKey());
        getEventOperation.putParameter("sLang", language);
        getEventOperation.putParameter("aIds", ids);
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
        final ArticlesIds that = (ArticlesIds)o;
        return this.m_ids.equals(that.m_ids);
    }
    
    @Override
    public int hashCode() {
        return this.m_ids.hashCode();
    }
    
    @Override
    public String toString() {
        return "ArticlesIds{m_ids=" + this.m_ids + '}';
    }
}
