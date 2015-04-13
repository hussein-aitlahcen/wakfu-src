package com.ankamagames.wakfu.client.core.game.webShop;

import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class WalletEntry extends ImmutableFieldProvider
{
    public static final String CURRENCY = "currency";
    public static final String CURRENCY_ID = "currencyId";
    public static final String VALUE = "value";
    public static final String DESCRIPTION = "description";
    private final WebShopSession m_session;
    private final Currency m_currency;
    
    public WalletEntry(final Currency currency, final WebShopSession session) {
        super();
        this.m_currency = currency;
        this.m_session = session;
    }
    
    @Override
    public String[] getFields() {
        return WalletEntry.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("currency")) {
            return this.m_currency.getIconUrl();
        }
        if (fieldName.equals("currencyId")) {
            return this.m_currency.getId();
        }
        if (fieldName.equals("value")) {
            return this.m_session.getWalletAmount(this.m_currency);
        }
        if (fieldName.equals("description")) {
            return this.m_currency.getDescription();
        }
        return null;
    }
    
    void updateValue() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "value");
    }
    
    @Override
    public String toString() {
        return "WalletEntry{m_currency=" + this.m_currency + '}';
    }
}
