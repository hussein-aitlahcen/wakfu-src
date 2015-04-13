package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import java.util.*;

public class HomeLoader extends SoapEntryLoader<Home, HomeAnswer>
{
    public static final HomeLoader INSTANCE;
    
    private HomeLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_SHOP_URL), new HomeAnswer());
    }
    
    public void getHome(final HomeListener listener) {
        AuthentificationManager.INSTANCE.getSessionId(new AuthentificationListener() {
            @Override
            public void onSessionId(final Map<String, List<String>> sessionId) {
                HomeLoader.this.setSessionID(sessionId);
                ((SoapEntryLoader<Home, U>)HomeLoader.this).sendRequest(new Home(listener));
            }
            
            @Override
            public void onError() {
                listener.onError();
            }
        });
    }
    
    @Override
    public String toString() {
        return "HomeLoader{}";
    }
    
    static {
        INSTANCE = new HomeLoader();
    }
}
