package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import java.util.*;

public class ArticlesIdsLoader extends SoapEntryLoader<ArticlesIds, ArticlesIdsAnswer>
{
    public static final ArticlesIdsLoader INSTANCE;
    
    private ArticlesIdsLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_SHOP_URL), new ArticlesIdsAnswer());
    }
    
    public void getArticlesByIds(@NotNull final TIntArrayList ids, final ArticlesIdsListener listener) {
        AuthentificationManager.INSTANCE.getSessionId(new AuthentificationListener() {
            @Override
            public void onSessionId(final Map<String, List<String>> sessionId) {
                ArticlesIdsLoader.this.setSessionID(sessionId);
                ((SoapEntryLoader<ArticlesIds, U>)ArticlesIdsLoader.this).sendRequest(new ArticlesIds(ids, listener));
            }
            
            @Override
            public void onError() {
                listener.onError();
            }
        });
    }
    
    static {
        INSTANCE = new ArticlesIdsLoader();
    }
}
