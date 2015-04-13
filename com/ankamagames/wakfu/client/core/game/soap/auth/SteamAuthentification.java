package com.ankamagames.wakfu.client.core.game.soap.auth;

import org.jetbrains.annotations.*;
import com.ankamagames.framework.net.soap.*;

public class SteamAuthentification implements SOAPRequestProvider
{
    private static final String STEAM_OP = "Steam";
    private static final String KEY = "sKey";
    private static final String STEAM_ID = "sSteamId";
    private static final String LANGUAGE = "sLang";
    private static final String COUNTRY = "sCountry";
    private static final String CURRENCY = "sCurrency";
    private final String m_token;
    private final String m_steamId;
    private final String m_lang;
    private final String m_country;
    private final String m_currency;
    
    public SteamAuthentification(@NotNull final String token, @NotNull final String steamId, @NotNull final String lang, @NotNull final String country, @NotNull final String currency) {
        super();
        this.m_token = token;
        this.m_steamId = steamId;
        this.m_lang = lang;
        this.m_country = country;
        this.m_currency = currency;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement("Steam");
        getEventOperation.putParameter("sKey", this.m_token);
        getEventOperation.putParameter("sSteamId", this.m_steamId);
        getEventOperation.putParameter("sLang", this.m_lang);
        getEventOperation.putParameter("sCurrency", this.m_currency);
        getEventOperation.putParameter("sCountry", this.m_country);
        AuthenticationHelper.addMetaData(getEventOperation);
        final SOAPBody soapBody = new SOAPBody();
        soapBody.addElement(getEventOperation);
        return soapBody;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SteamAuthentification that = (SteamAuthentification)o;
        return this.m_country.equals(that.m_country) && this.m_currency.equals(that.m_currency) && this.m_lang.equals(that.m_lang) && this.m_steamId.equals(that.m_steamId) && this.m_token.equals(that.m_token);
    }
    
    @Override
    public int hashCode() {
        int result = this.m_token.hashCode();
        result = 31 * result + this.m_steamId.hashCode();
        result = 31 * result + this.m_lang.hashCode();
        result = 31 * result + this.m_country.hashCode();
        result = 31 * result + this.m_currency.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        return "SteamAuthentification{m_token='" + this.m_token + '\'' + ", m_steamId='" + this.m_steamId + '\'' + ", m_lang='" + this.m_lang + '\'' + ", m_country='" + this.m_country + '\'' + ", m_currency='" + this.m_currency + '\'' + '}';
    }
}
