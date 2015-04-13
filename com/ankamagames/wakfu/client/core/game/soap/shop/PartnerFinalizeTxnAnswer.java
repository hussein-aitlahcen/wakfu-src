package com.ankamagames.wakfu.client.core.game.soap.shop;

import org.apache.log4j.*;
import com.ankamagames.framework.net.soap.data.*;
import com.ankamagames.framework.net.soap.*;

public class PartnerFinalizeTxnAnswer implements SOAPAnswerProcessor<PartnerFinalizeTxn>
{
    private static final Logger m_logger;
    private static final String RESPONSE = "PartnerFinalizeTransactionResponse";
    private static final String ERROR = "error";
    private static final String RESULT = "result";
    private static final String OGRINS = "ogrins";
    private static final String KROSZ = "krozs";
    
    @Override
    public void process(final SOAPEnvelope envelope, final PartnerFinalizeTxn provider) {
        final SOAPElement getEventResponse = envelope.getBody().getElement("PartnerFinalizeTransactionResponse");
        final MapData data = (MapData)getEventResponse.getFirstData();
        if (!data.getValue("result").getBooleanValue()) {
            provider.getListener().onError(PartnerFinalizeTxnError.getFromAPICode(data.getValue("error").getStringValue()));
            return;
        }
        try {
            final int ogrins = data.getValue("ogrins").getIntValue();
            final int krozs = data.getValue("krozs").getIntValue();
            provider.getListener().onPartnerFinalizeTxn(ogrins, krozs);
        }
        catch (Exception e) {
            PartnerFinalizeTxnAnswer.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation des donn\u00e9es d'un achat rapide", (Throwable)e);
        }
    }
    
    @Override
    public void onError(final PartnerFinalizeTxn provider) {
        provider.getListener().onError(PartnerFinalizeTxnError.UNKNOWN);
    }
    
    static {
        m_logger = Logger.getLogger((Class)PartnerFinalizeTxnAnswer.class);
    }
}
