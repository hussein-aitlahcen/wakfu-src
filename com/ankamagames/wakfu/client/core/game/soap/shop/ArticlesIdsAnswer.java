package com.ankamagames.wakfu.client.core.game.soap.shop;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.framework.net.soap.data.*;
import com.ankamagames.framework.net.soap.*;

public class ArticlesIdsAnswer implements SOAPAnswerProcessor<ArticlesIds>
{
    private static final Logger m_logger;
    private static final String ARTICLES_IDS_RESPONSE = "ArticlesIdsResponse";
    private static final String RESULT = "result";
    private static final String ARTICLES = "articles";
    
    @Override
    public void process(final SOAPEnvelope envelope, final ArticlesIds provider) {
        final SOAPElement getEventResponse = envelope.getBody().getElement("ArticlesIdsResponse");
        final MapData data = (MapData)getEventResponse.getFirstData();
        if (!data.getValue("result").getBooleanValue()) {
            provider.getListener().onError();
            return;
        }
        try {
            final ArrayList<Article> lists = new ArrayList<Article>();
            final ArrayData articles = (ArrayData)data.getValue("articles");
            for (int i = 0, size = articles.size(); i < size; ++i) {
                final MapData articleData = (MapData)articles.getValue(i);
                final Article article = ShopHelper.createArticle(articleData);
                if (article.isServerRestrictionOK()) {
                    lists.add(article);
                }
            }
            provider.getListener().onArticlesIds(lists);
        }
        catch (Exception e) {
            ArticlesIdsAnswer.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation des donn\u00e9es de liste d'articles", (Throwable)e);
        }
    }
    
    @Override
    public void onError(final ArticlesIds provider) {
        provider.getListener().onError();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ArticlesIdsAnswer.class);
    }
}
