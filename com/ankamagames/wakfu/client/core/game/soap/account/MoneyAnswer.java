package com.ankamagames.wakfu.client.core.game.soap.account;

import org.apache.log4j.*;
import com.ankamagames.framework.net.soap.data.*;
import com.ankamagames.framework.net.soap.*;

public class MoneyAnswer implements SOAPAnswerProcessor<Money>
{
    private static final Logger m_logger;
    private static final String MONEY_RESPONSE = "MoneyResponse";
    private static final String RESULT = "result";
    private static final String OGRINS = "ogrins";
    private static final String KROZS = "krozs";
    
    @Override
    public void process(final SOAPEnvelope envelope, final Money provider) {
        final SOAPElement getEventResponse = envelope.getBody().getElement("MoneyResponse");
        final MapData data = (MapData)getEventResponse.getFirstData();
        if (!data.getValue("result").getBooleanValue()) {
            return;
        }
        try {
            final int ogrins = data.getValue("ogrins").getIntValue();
            final int krozs = data.getValue("krozs").getIntValue();
            provider.getListener().onMoney(ogrins, krozs);
        }
        catch (Exception e) {
            MoneyAnswer.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation des donn\u00e9es d'argent", (Throwable)e);
        }
    }
    
    @Override
    public void onError(final Money provider) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)MoneyAnswer.class);
    }
}
