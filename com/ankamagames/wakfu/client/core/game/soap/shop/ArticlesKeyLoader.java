package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import java.util.*;

public class ArticlesKeyLoader extends SoapEntryLoader<ArticlesKey, ArticlesKeyAnswer>
{
    public static final ArticlesKeyLoader INSTANCE;
    
    private ArticlesKeyLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_SHOP_URL), new ArticlesKeyAnswer());
    }
    
    public void getArticlesKey(final String key, final ArticlesKeyListener listener) {
        AuthentificationManager.INSTANCE.getSessionId(new AuthentificationListener() {
            @Override
            public void onSessionId(final Map<String, List<String>> sessionId) {
                ArticlesKeyLoader.this.setSessionID(sessionId);
                ((SoapEntryLoader<ArticlesKey, U>)ArticlesKeyLoader.this).sendRequest(new ArticlesKey(listener, key));
            }
            
            @Override
            public void onError() {
                listener.onError();
            }
        });
    }
    
    @Override
    public String toString() {
        return "ArticlesKeyLoader{}";
    }
    
    static {
        INSTANCE = new ArticlesKeyLoader();
    }
}
