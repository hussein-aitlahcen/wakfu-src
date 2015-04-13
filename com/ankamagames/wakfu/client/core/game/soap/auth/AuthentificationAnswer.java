package com.ankamagames.wakfu.client.core.game.soap.auth;

import org.apache.log4j.*;
import com.ankamagames.framework.net.soap.data.*;
import java.util.*;
import com.ankamagames.framework.net.soap.*;

public class AuthentificationAnswer implements SOAPAnswerProcessor<Authentification>
{
    private static final Logger m_logger;
    private static final String AUTHENTIFICATION_RESPONSE = "AuthentificationResponse";
    
    @Override
    public void process(final SOAPEnvelope envelope, final Authentification provider) {
        final SOAPElement getEventResponse = envelope.getBody().getElement("AuthentificationResponse");
        final MapData data = (MapData)getEventResponse.getFirstData();
        if (!data.getValue("success").getBooleanValue()) {
            AuthentificationManager.INSTANCE.setSessionParams(null);
            return;
        }
        try {
            AuthentificationManager.INSTANCE.setSessionParams(envelope.getHeaderFields());
        }
        catch (Exception e) {
            AuthentificationAnswer.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation des donn\u00e9es d'authentification", (Throwable)e);
        }
    }
    
    @Override
    public void onError(final Authentification provider) {
        AuthentificationManager.INSTANCE.setSessionParams(null);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AuthentificationAnswer.class);
    }
}
