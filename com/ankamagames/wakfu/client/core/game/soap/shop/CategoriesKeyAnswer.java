package com.ankamagames.wakfu.client.core.game.soap.shop;

import org.apache.log4j.*;
import com.ankamagames.framework.net.soap.data.*;
import com.ankamagames.framework.net.soap.*;

public class CategoriesKeyAnswer implements SOAPAnswerProcessor<CategoriesKey>
{
    private static final Logger m_logger;
    private static final String CATEGORIES_KEY_RESPONSE = "CategoriesKeyResponse";
    private static final String RESULT = "result";
    
    @Override
    public void process(final SOAPEnvelope envelope, final CategoriesKey provider) {
        final SOAPElement getEventResponse = envelope.getBody().getElement("CategoriesKeyResponse");
        final MapData data = (MapData)getEventResponse.getFirstData();
        if (!data.getValue("result").getBooleanValue()) {
            provider.getListener().onError();
            return;
        }
        try {
            provider.getListener().onCategoriesKey(ShopHelper.createCategoryList(data));
        }
        catch (Exception e) {
            CategoriesKeyAnswer.m_logger.warn((Object)"Probl\u00e8me \u00e0 la d\u00e9s\u00e9rialisation des donn\u00e9es de liste de categories", (Throwable)e);
            provider.getListener().onError();
        }
    }
    
    @Override
    public void onError(final CategoriesKey provider) {
        provider.getListener().onError();
    }
    
    static {
        m_logger = Logger.getLogger((Class)CategoriesKeyAnswer.class);
    }
}
