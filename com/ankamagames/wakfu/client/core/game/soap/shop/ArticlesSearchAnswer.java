package com.ankamagames.wakfu.client.core.game.soap.shop;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.framework.net.soap.data.*;
import com.ankamagames.framework.net.soap.*;

public class ArticlesSearchAnswer implements SOAPAnswerProcessor<ArticlesSearch>
{
    private static final Logger m_logger;
    private static final String ARTICLES_SEARCH_RESPONSE = "ArticlesSearchResponse";
    private static final String RESULT = "result";
    private static final String ARTICLES = "articles";
    private static final String COUNT = "count";
    
    @Override
    public void process(final SOAPEnvelope envelope, final ArticlesSearch provider) {
        final SOAPElement getEventResponse = envelope.getBody().getElement("ArticlesSearchResponse");
        final MapData data = (MapData)getEventResponse.getFirstData();
        if (!data.getValue("result").getBooleanValue()) {
            provider.getListener().onError();
            return;
        }
        try {
            final ArrayList<Article> lists = new ArrayList<Article>();
            final ArrayData articles = (ArrayData)data.getValue("articles");
            final int count = data.getIntValue("count");
            for (int i = 0, size = articles.size(); i < size; ++i) {
                final MapData articleData = (MapData)articles.getValue(i);
                final Article article = ShopHelper.createArticle(articleData);
                if (article.isServerRestrictionOK()) {
                    lists.add(article);
                }
            }
            provider.getListener().onArticlesSearch(lists, count);
        }
        catch (Exception e) {
            ArticlesSearchAnswer.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation des donn\u00e9es de liste d'articles", (Throwable)e);
        }
    }
    
    @Override
    public void onError(final ArticlesSearch provider) {
        provider.getListener().onError();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ArticlesSearchAnswer.class);
    }
}
