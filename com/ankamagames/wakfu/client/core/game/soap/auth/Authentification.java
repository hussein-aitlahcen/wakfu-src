package com.ankamagames.wakfu.client.core.game.soap.auth;

import org.jetbrains.annotations.*;
import com.ankamagames.framework.net.soap.*;

public class Authentification implements SOAPRequestProvider
{
    private static final String AUTHENTIFICATION_OP = "Authentification";
    private static final String KEY = "sKey";
    private final String m_token;
    
    public Authentification(@NotNull final String token) {
        super();
        this.m_token = token;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement("Authentification");
        getEventOperation.putParameter("sKey", this.m_token);
        AuthenticationHelper.addMetaData(getEventOperation);
        final SOAPBody soapBody = new SOAPBody();
        soapBody.addElement(getEventOperation);
        return soapBody;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final Authentification that = (Authentification)obj;
        return this.m_token.equals(that.m_token);
    }
    
    @Override
    public int hashCode() {
        return this.m_token.hashCode();
    }
    
    @Override
    public String toString() {
        return "Authentification{m_token='" + this.m_token + '\'' + '}';
    }
}
