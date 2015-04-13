package com.ankamagames.wakfu.client.core.game.soap.auth;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.common.configuration.*;

public class SteamAuthentificationLoader extends SoapEntryLoader<SteamAuthentification, SteamAuthentificationAnswer>
{
    public static final SteamAuthentificationLoader INSTANCE;
    
    private SteamAuthentificationLoader() {
        super(SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SOAP_AUTHENTICATION_URL), new SteamAuthentificationAnswer());
    }
    
    public void sendRequest(final String iceToken, final String steamId, final String lang, final String country, final String currency) {
        ((SoapEntryLoader<SteamAuthentification, U>)this).sendRequest(new SteamAuthentification(iceToken, steamId, lang, country, currency));
    }
    
    @Override
    public String toString() {
        return "AuthentificationLoader{}";
    }
    
    static {
        INSTANCE = new SteamAuthentificationLoader();
    }
}
