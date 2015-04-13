package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import java.util.*;

public class QuickBuyLoader extends SoapEntryLoader<QuickBuy, QuickBuyAnswer>
{
    public static final QuickBuyLoader INSTANCE;
    
    private QuickBuyLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_SHOP_URL), new QuickBuyAnswer());
    }
    
    public void quickBuy(final int articleId, final int quantity, final QuickBuyListener listener) {
        AuthentificationManager.INSTANCE.getSessionId(new AuthentificationListener() {
            @Override
            public void onSessionId(final Map<String, List<String>> sessionId) {
                QuickBuyLoader.this.setSessionID(sessionId);
                ((SoapEntryLoader<QuickBuy, U>)QuickBuyLoader.this).sendRequest(new QuickBuy(articleId, quantity, listener));
            }
            
            @Override
            public void onError() {
                listener.onError(QuickBuyError.UNKNOWN);
            }
        });
    }
    
    @Override
    public String toString() {
        return "QuickBuyLoader{}";
    }
    
    static {
        INSTANCE = new QuickBuyLoader();
    }
}
