package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import java.util.*;

public class PartnerFinalizeTxnLoader extends SoapEntryLoader<PartnerFinalizeTxn, PartnerFinalizeTxnAnswer>
{
    public static final PartnerFinalizeTxnLoader INSTANCE;
    
    private PartnerFinalizeTxnLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_SHOP_URL), new PartnerFinalizeTxnAnswer());
    }
    
    public void partnerFinalizeTxn(final int orderId, final boolean finalize, final PartnerFinalizeTxnListener listener) {
        AuthentificationManager.INSTANCE.getSessionId(new AuthentificationListener() {
            @Override
            public void onSessionId(final Map<String, List<String>> sessionId) {
                PartnerFinalizeTxnLoader.this.setSessionID(sessionId);
                ((SoapEntryLoader<PartnerFinalizeTxn, U>)PartnerFinalizeTxnLoader.this).sendRequest(new PartnerFinalizeTxn(orderId, finalize, listener));
            }
            
            @Override
            public void onError() {
                listener.onError(PartnerFinalizeTxnError.UNKNOWN);
            }
        });
    }
    
    static {
        INSTANCE = new PartnerFinalizeTxnLoader();
    }
}
