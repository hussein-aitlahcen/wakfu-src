package com.ankamagames.wakfu.client.core.game.soap.shop;

import org.jetbrains.annotations.*;

public enum PartnerFinalizeTxnError
{
    UNKNOWN(""), 
    PARTNER_MISSING("PARTNER_MISSING"), 
    PARTNER_NOORDER("PARTNER_NOORDER"), 
    PARTNER_ERROR("PARTNER_ERROR"), 
    QUICK_BUY_PAID_FAILED("QUICKBUY_PAIDFAILED"), 
    QUICK_BUY_CANCEL_FAILED("QUICKBUY_CANCELFAILED");
    
    private final String m_apiCode;
    
    private PartnerFinalizeTxnError(final String apiCode) {
        this.m_apiCode = apiCode;
    }
    
    @Nullable
    public static PartnerFinalizeTxnError getFromAPICode(final String code) {
        for (final PartnerFinalizeTxnError error : values()) {
            if (error.m_apiCode.equals(code)) {
                return error;
            }
        }
        return null;
    }
}
