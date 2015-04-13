package com.ankamagames.wakfu.client.core.game.soap.auth;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;

public class AuthentificationLoader extends SoapEntryLoader<Authentification, AuthentificationAnswer>
{
    public static final AuthentificationLoader INSTANCE;
    
    private AuthentificationLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_AUTHENTICATION_URL), new AuthentificationAnswer());
    }
    
    public void sendRequest(final String iceToken) {
        ((SoapEntryLoader<Authentification, U>)this).sendRequest(new Authentification(iceToken));
    }
    
    @Override
    public String toString() {
        return "AuthentificationLoader{}";
    }
    
    static {
        INSTANCE = new AuthentificationLoader();
    }
}
