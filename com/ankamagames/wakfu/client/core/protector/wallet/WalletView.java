package com.ankamagames.wakfu.client.core.protector.wallet;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class WalletView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String AMOUNT = "amount";
    public static final String AMOUNT_VALUE = "amountValue";
    private ProtectorWalletContext m_context;
    private ProtectorWalletHandler<Protector> m_handler;
    
    public WalletView(final ProtectorWalletContext context, final ProtectorWalletHandler<Protector> handler) {
        super();
        this.m_context = context;
        this.m_handler = handler;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString("protector.wallet.context.name." + this.m_context.idx);
        }
        if (fieldName.equals("amount")) {
            return this.m_handler.getAmountOfCash(this.m_context) + "§";
        }
        if (fieldName.equals("amountValue")) {
            return this.m_handler.getAmountOfCash(this.m_context);
        }
        return null;
    }
    
    public ProtectorWalletContext getContext() {
        return this.m_context;
    }
    
    public ProtectorWalletHandler<Protector> getHandler() {
        return this.m_handler;
    }
    
    void updateProperty() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "amount", "amountValue");
    }
}
