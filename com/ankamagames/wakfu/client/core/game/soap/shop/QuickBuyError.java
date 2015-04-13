package com.ankamagames.wakfu.client.core.game.soap.shop;

import org.jetbrains.annotations.*;

public enum QuickBuyError
{
    UNKNOWN(""), 
    STOCK_MISSING("STOCKMISSING"), 
    TRANSACTION_FAILED("PAIDFAILED"), 
    NOT_ENOUGH_MONEY("MISSINGMONEY"), 
    NO_PRICE("NOPRICE"), 
    PARTNER_ERROR("PARTNER_ERROR");
    
    private final String m_apiCode;
    
    private QuickBuyError(final String apiCode) {
        this.m_apiCode = apiCode;
    }
    
    @Nullable
    public static QuickBuyError getFromAPICode(final String code) {
        for (final QuickBuyError error : values()) {
            if (error.m_apiCode.equals(code)) {
                return error;
            }
        }
        return null;
    }
}
