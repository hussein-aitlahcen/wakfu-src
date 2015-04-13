package com.ankamagames.wakfu.client.core.protector.wallet;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.reflect.*;

public class WalletHandlerView extends ImmutableFieldProvider implements WalletEventListener
{
    public static final String WALLETS = "wallets";
    public static final String TOTAL_AMOUNT = "totalAmount";
    public static final String CHALLENGE = "challenge";
    public static final String WEATHER = "weather";
    public static final String ECOSYSTEM = "ecosystem";
    private ProtectorWalletHandler<Protector> m_handler;
    private ArrayList<WalletView> m_views;
    
    public WalletHandlerView(final Protector protector, final ProtectorWalletHandler<Protector> handler) {
        super();
        this.m_views = new ArrayList<WalletView>();
        this.m_handler = handler;
        this.reinitialize(protector);
    }
    
    public void reinitialize(final Protector protector) {
        for (final ProtectorWalletContext context : ProtectorWalletContext.values()) {
            switch (context) {
                case CLIMATE: {
                    if (protector.getClimateMerchantInventory().size() != 0) {
                        this.m_views.add(new WalletView(context, this.m_handler));
                        break;
                    }
                    break;
                }
                case CHALLENGE: {
                    if (protector.getChallengeMerchantInventory().size() != 0) {
                        this.m_views.add(new WalletView(context, this.m_handler));
                        break;
                    }
                    break;
                }
                case ECOSYSTEM: {
                    if (PropertiesProvider.getInstance().getBooleanProperty("wakfuEcosystemEnabled")) {
                        this.m_views.add(new WalletView(context, this.m_handler));
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    private WalletView getViewFromContext(final ProtectorWalletContext context) {
        for (int i = 0, size = this.m_views.size(); i < size; ++i) {
            final WalletView walletView = this.m_views.get(i);
            if (walletView.getContext() == context) {
                return walletView;
            }
        }
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("wallets")) {
            return this.m_views;
        }
        if (fieldName.equals("challenge")) {
            return this.getViewFromContext(ProtectorWalletContext.CHALLENGE);
        }
        if (fieldName.equals("weather")) {
            return this.getViewFromContext(ProtectorWalletContext.CLIMATE);
        }
        if (fieldName.equals("ecosystem")) {
            return this.getViewFromContext(ProtectorWalletContext.ECOSYSTEM);
        }
        if (fieldName.equals("totalAmount")) {
            return this.m_handler.getAmountOfCash() + "§";
        }
        return null;
    }
    
    @Override
    public void onWalletUpdated(final Wallet wallet, final int delta) {
        this.updateView();
    }
    
    public void updateView() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "wallets", "totalAmount");
        final WalletView challenge = this.getViewFromContext(ProtectorWalletContext.CHALLENGE);
        if (challenge != null) {
            challenge.updateProperty();
        }
        final WalletView climate = this.getViewFromContext(ProtectorWalletContext.CLIMATE);
        if (climate != null) {
            climate.updateProperty();
        }
        final WalletView ecosystem = this.getViewFromContext(ProtectorWalletContext.ECOSYSTEM);
        if (ecosystem != null) {
            ecosystem.updateProperty();
        }
    }
}
