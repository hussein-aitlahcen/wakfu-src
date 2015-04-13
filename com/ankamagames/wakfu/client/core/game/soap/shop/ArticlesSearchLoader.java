package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import java.util.*;

public class ArticlesSearchLoader extends SoapEntryLoader<ArticlesSearch, ArticlesSearchAnswer>
{
    public static final ArticlesSearchLoader INSTANCE;
    
    private ArticlesSearchLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_SHOP_URL), new ArticlesSearchAnswer());
    }
    
    public void getArticlesSearch(final TIntArrayList category, final String text, final int page, final int size, final ArticlesSearchListener listener) {
        AuthentificationManager.INSTANCE.getSessionId(new AuthentificationListener() {
            @Override
            public void onSessionId(final Map<String, List<String>> sessionId) {
                ArticlesSearchLoader.this.setSessionID(sessionId);
                ((SoapEntryLoader<ArticlesSearch, U>)ArticlesSearchLoader.this).sendRequest(new ArticlesSearch(category, page, text, size, listener));
            }
            
            @Override
            public void onError() {
                listener.onError();
            }
        });
    }
    
    @Override
    public String toString() {
        return "ArticlesSearchLoader{}";
    }
    
    static {
        INSTANCE = new ArticlesSearchLoader();
    }
}
