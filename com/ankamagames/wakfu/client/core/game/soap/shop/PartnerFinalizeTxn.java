package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.client.core.*;

public class PartnerFinalizeTxn implements SOAPRequestProvider
{
    private static final String OPERATION = "PartnerFinalizeTransaction";
    private static final String SHOP_KEY = "sShopKey";
    private static final String LANG = "sLang";
    private static final String FINALIZE = "bFinalize";
    private static final String ORDER_ID = "iOrderId";
    private final int m_orderId;
    private final boolean m_finalize;
    private final PartnerFinalizeTxnListener m_listener;
    
    public PartnerFinalizeTxn(final int orderId, final boolean finalize, final PartnerFinalizeTxnListener listener) {
        super();
        this.m_orderId = orderId;
        this.m_finalize = finalize;
        this.m_listener = listener;
    }
    
    @Override
    public SOAPBody createRequest() {
        final SOAPElement getEventOperation = new SOAPElement("PartnerFinalizeTransaction");
        final String language = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage();
        getEventOperation.putParameter("sShopKey", ShopConstants.getShopKey());
        getEventOperation.putParameter("sLang", language);
        getEventOperation.putParameter("bFinalize", this.m_finalize);
        getEventOperation.putParameter("iOrderId", this.m_orderId);
        final SOAPBody soapBody = new SOAPBody();
        soapBody.addElement(getEventOperation);
        return soapBody;
    }
    
    public PartnerFinalizeTxnListener getListener() {
        return this.m_listener;
    }
    
    @Override
    public String toString() {
        return "PartnerFinalizeTxn{m_orderId=" + this.m_orderId + ", m_finalize=" + this.m_finalize + '}';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PartnerFinalizeTxn that = (PartnerFinalizeTxn)o;
        return this.m_orderId == that.m_orderId;
    }
    
    @Override
    public int hashCode() {
        return this.m_orderId ^ this.m_orderId >>> 32;
    }
}
