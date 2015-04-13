package com.ankamagames.wakfu.client.core.game.soap.shop;

import org.apache.log4j.*;
import com.ankamagames.framework.net.soap.data.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.framework.net.soap.*;

public class ArticlesListAnswer implements SOAPAnswerProcessor<ArticlesList>
{
    private static final Logger m_logger;
    private static final String ARTICLES_LIST_RESPONSE = "ArticlesListResponse";
    private static final String ARTICLES_GONDOLAHEAD_RESPONSE = "ArticlesGondolaheadResponse";
    private static final String RESULT = "result";
    private static final String COUNT = "count";
    
    @Override
    public void process(final SOAPEnvelope envelope, final ArticlesList provider) {
        SOAPElement getEventResponse = envelope.getBody().getElement("ArticlesListResponse");
        if (getEventResponse == null) {
            getEventResponse = envelope.getBody().getElement("ArticlesGondolaheadResponse");
        }
        final MapData data = (MapData)getEventResponse.getFirstData();
        if (!data.getValue("result").getBooleanValue()) {
            provider.getListener().onError();
            return;
        }
        try {
            final int count = data.getIntValue("count");
            final ArrayList<Article> lists = ShopHelper.createArticlesList(data);
            provider.getListener().onArticlesList(lists, count);
        }
        catch (Exception e) {
            ArticlesListAnswer.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation des donn\u00e9es de liste d'articles", (Throwable)e);
        }
    }
    
    @Override
    public void onError(final ArticlesList provider) {
        provider.getListener().onError();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ArticlesListAnswer.class);
    }
}
