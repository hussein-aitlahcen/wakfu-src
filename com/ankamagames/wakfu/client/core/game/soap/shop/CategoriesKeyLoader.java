package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import java.util.*;

public class CategoriesKeyLoader extends SoapEntryLoader<CategoriesKey, CategoriesKeyAnswer>
{
    public static final CategoriesKeyLoader INSTANCE;
    
    private CategoriesKeyLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_SHOP_URL), new CategoriesKeyAnswer());
    }
    
    public void getCategoriesKey(final String key, final CategoriesKeyListener listener) {
        AuthentificationManager.INSTANCE.getSessionId(new AuthentificationListener() {
            @Override
            public void onSessionId(final Map<String, List<String>> sessionId) {
                CategoriesKeyLoader.this.setSessionID(sessionId);
                ((SoapEntryLoader<CategoriesKey, U>)CategoriesKeyLoader.this).sendRequest(new CategoriesKey(listener, key));
            }
            
            @Override
            public void onError() {
                listener.onError();
            }
        });
    }
    
    @Override
    public String toString() {
        return "CategoriesKeyLoader{}";
    }
    
    static {
        INSTANCE = new CategoriesKeyLoader();
    }
}
