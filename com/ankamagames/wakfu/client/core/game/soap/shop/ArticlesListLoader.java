package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import java.util.*;

public class ArticlesListLoader extends SoapEntryLoader<ArticlesList, ArticlesListAnswer>
{
    public static final ArticlesListLoader INSTANCE;
    
    private ArticlesListLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_SHOP_URL), new ArticlesListAnswer());
    }
    
    public void getArticlesList(final int category, final int page, final int size, final ArticlesListListener listener) {
        AuthentificationManager.INSTANCE.getSessionId(new AuthentificationListener() {
            @Override
            public void onSessionId(final Map<String, List<String>> sessionId) {
                ArticlesListLoader.this.setSessionID(sessionId);
                ((SoapEntryLoader<ArticlesList, U>)ArticlesListLoader.this).sendRequest(new ArticlesList(category, page, size, listener));
            }
            
            @Override
            public void onError() {
                listener.onError();
            }
        });
    }
    
    @Override
    public String toString() {
        return "ArticlesListLoader{}";
    }
    
    static {
        INSTANCE = new ArticlesListLoader();
    }
}
