package com.ankamagames.wakfu.client.core.game.soap.account;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import java.util.*;

public class MoneyLoader extends SoapEntryLoader<Money, MoneyAnswer>
{
    public static final MoneyLoader INSTANCE;
    
    private MoneyLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_ACCOUNT_URL), new MoneyAnswer());
    }
    
    public void getMoney(final MoneyListener listener) {
        AuthentificationManager.INSTANCE.getSessionId(new AuthentificationListener() {
            @Override
            public void onSessionId(final Map<String, List<String>> sessionId) {
                MoneyLoader.this.setSessionID(sessionId);
                ((SoapEntryLoader<Money, U>)MoneyLoader.this).sendRequest(new Money(listener));
            }
            
            @Override
            public void onError() {
                listener.onError();
            }
        });
    }
    
    @Override
    public String toString() {
        return "MoneyLoader{}";
    }
    
    static {
        INSTANCE = new MoneyLoader();
    }
}
