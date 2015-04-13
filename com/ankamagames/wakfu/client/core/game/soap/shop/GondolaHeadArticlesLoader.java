package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import java.util.*;

public class GondolaHeadArticlesLoader extends SoapEntryLoader<ArticlesList, ArticlesListAnswer>
{
    public static final GondolaHeadArticlesLoader INSTANCE;
    
    private GondolaHeadArticlesLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_SHOP_URL), new ArticlesListAnswer());
    }
    
    public void getArticlesList(final int gondolaHeadId, final int page, final int size, final ArticlesListListener listener) {
        AuthentificationManager.INSTANCE.getSessionId(new AuthentificationListener() {
            @Override
            public void onSessionId(final Map<String, List<String>> sessionId) {
                GondolaHeadArticlesLoader.this.setSessionID(sessionId);
                ((SoapEntryLoader<ArticlesList, U>)GondolaHeadArticlesLoader.this).sendRequest(new ArticlesList(gondolaHeadId, page, size, listener, true));
            }
            
            @Override
            public void onError() {
                listener.onError();
            }
        });
    }
    
    @Override
    public String toString() {
        return "GondolaHeadArticlesLoader{}";
    }
    
    static {
        INSTANCE = new GondolaHeadArticlesLoader();
    }
}
