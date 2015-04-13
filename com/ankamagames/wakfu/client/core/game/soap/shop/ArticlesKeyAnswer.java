package com.ankamagames.wakfu.client.core.game.soap.shop;

import org.apache.log4j.*;
import com.ankamagames.framework.net.soap.data.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import java.util.*;
import com.ankamagames.framework.net.soap.*;

public class ArticlesKeyAnswer implements SOAPAnswerProcessor<ArticlesKey>
{
    private static final Logger m_logger;
    private static final String ARTICLES_KEY_RESPONSE = "ArticlesKeyResponse";
    private static final String RESULT = "result";
    
    @Override
    public void process(final SOAPEnvelope envelope, final ArticlesKey provider) {
        final SOAPElement getEventResponse = envelope.getBody().getElement("ArticlesKeyResponse");
        final MapData data = (MapData)getEventResponse.getFirstData();
        if (!data.getValue("result").getBooleanValue()) {
            provider.getListener().onError();
            return;
        }
        try {
            final ArrayList<Article> lists = ShopHelper.createArticlesList(data);
            provider.getListener().onArticlesKey(lists);
        }
        catch (Exception e) {
            ArticlesKeyAnswer.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation des donn\u00e9es de liste d'articles", (Throwable)e);
        }
    }
    
    @Override
    public void onError(final ArticlesKey provider) {
        provider.getListener().onError();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ArticlesKeyAnswer.class);
    }
}
