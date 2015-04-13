package com.ankamagames.wakfu.client.core.game.soap.shop;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.net.soap.data.*;
import com.ankamagames.framework.net.soap.*;

public class QuickBuyAnswer implements SOAPAnswerProcessor<QuickBuy>
{
    private static final Logger m_logger;
    private static final String QUICK_BUY_RESPONSE = "QuickBuyResponse";
    private static final String OGRINS = "ogrins";
    private static final String KROSZ = "krozs";
    private static final String ERROR = "error";
    private static final String RESULT = "result";
    private static final String RESPONSE = "response";
    
    @Override
    public void process(final SOAPEnvelope envelope, final QuickBuy provider) {
        final SOAPElement getEventResponse = envelope.getBody().getElement("QuickBuyResponse");
        final MapData data = (MapData)getEventResponse.getFirstData();
        try {
            if (!data.getValue("result").getBooleanValue()) {
                final Data responseData = data.getValue("response");
                if (responseData != null) {
                    if (responseData.getDataType() == DataType.STRING) {
                        QuickBuyAnswer.m_logger.error((Object)("R\u00e9ponse : " + responseData.getStringValue()));
                    }
                    else if (responseData.getDataType() == DataType.MAP) {
                        final StringBuilder sb = new StringBuilder();
                        final MapData response = (MapData)responseData;
                        response.forEach(new TObjectObjectProcedure<String, Data>() {
                            @Override
                            public boolean execute(final String a, final Data b) {
                                sb.append('[').append(a).append("] => ").append(b.getStringValue());
                                return true;
                            }
                        });
                        QuickBuyAnswer.m_logger.warn((Object)("Error : " + (Object)sb));
                    }
                }
                final Data errorData = data.getValue("error");
                provider.getListener().onError((errorData != null) ? QuickBuyError.getFromAPICode(errorData.getStringValue()) : QuickBuyError.UNKNOWN);
                return;
            }
            final MapData response2 = (MapData)data.getValue("response");
            if (response2 != null) {
                provider.getListener().onPartnerQuickBuy();
            }
            else {
                final int ogrins = data.getValue("ogrins").getIntValue();
                final int krozs = data.getValue("krozs").getIntValue();
                provider.getListener().onQuickBuy(ogrins, krozs);
            }
        }
        catch (Exception e) {
            QuickBuyAnswer.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation des donn\u00e9es d'un achat rapide", (Throwable)e);
            provider.getListener().onError(QuickBuyError.UNKNOWN);
        }
    }
    
    @Override
    public void onError(final QuickBuy provider) {
        provider.getListener().onError(QuickBuyError.UNKNOWN);
    }
    
    static {
        m_logger = Logger.getLogger((Class)QuickBuyAnswer.class);
    }
}
