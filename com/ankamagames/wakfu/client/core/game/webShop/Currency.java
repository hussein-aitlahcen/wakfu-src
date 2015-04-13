package com.ankamagames.wakfu.client.core.game.webShop;

import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;

public enum Currency
{
    OGR(0, "OGR", (String)null, false), 
    KRZ(1, "KRZ", (String)null, false), 
    USD(2, "USD", "USD", true), 
    EUR(3, "EUR", "EUR", true), 
    GBP(4, "GBP", "GBP", true), 
    BRL(5, "BRL", "BRL", true), 
    RUB(6, "RUB", "RUB", true), 
    AUD(7, "AUD", "AUD", true), 
    CAD(8, "CAD", "CAD", true), 
    IDR(9, "IDR", "IDR", true), 
    JPY(10, "JPY", "JPY", true), 
    MYR(11, "MYR", "MYR", true), 
    MXN(12, "MXN", "MXN", true), 
    NZD(13, "NZD", "NZD", true), 
    NOK(14, "NOK", "NOK", true), 
    PHP(15, "PHP", "PHP", true), 
    SGD(16, "SGD", "SGD", true), 
    KRW(17, "KRW", "KRW", true), 
    THB(18, "THB", "THB", true), 
    TRY(19, "TRY", "TRY", true), 
    UAH(20, "UAH", "UAH", true);
    
    private static final Map<String, Currency> BY_API_KEY;
    private static final Map<String, Currency> BY_ISO_NAME;
    private final byte m_id;
    private final String m_apiKey;
    private final String m_isoName;
    private final boolean m_hardCurrency;
    
    private Currency(@Nullable final int id, final String apiKey, final String isoName, final boolean hardCurrency) {
        this.m_isoName = isoName;
        this.m_hardCurrency = hardCurrency;
        this.m_id = MathHelper.ensureByte(id);
        this.m_apiKey = apiKey;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    @NotNull
    public String getApiKey() {
        return this.m_apiKey;
    }
    
    public String getIconUrl() {
        return WakfuConfiguration.getInstance().getIconUrl("currencyIconUrl", "defaultIconPath", this.m_id);
    }
    
    public String getIsoName() {
        return this.m_isoName;
    }
    
    public boolean isHardCurrency() {
        return this.m_hardCurrency;
    }
    
    public String getDescription() {
        return "";
    }
    
    @Nullable
    public static Currency getByApiName(final String name) {
        return Currency.BY_API_KEY.get(name);
    }
    
    @Nullable
    public static Currency getByIsoName(final String name) {
        return getByIsoName(name, true);
    }
    
    public static Currency getByIsoName(final String name, final boolean withNulls) {
        final Currency currency = Currency.BY_ISO_NAME.get(name);
        return (currency != null || withNulls) ? currency : Currency.USD;
    }
    
    static {
        BY_API_KEY = new THashMap<String, Currency>();
        BY_ISO_NAME = new THashMap<String, Currency>();
        for (final Currency c : values()) {
            Currency.BY_API_KEY.put(c.m_apiKey, c);
            if (c.m_isoName != null) {
                Currency.BY_ISO_NAME.put(c.m_isoName, c);
            }
        }
    }
}
