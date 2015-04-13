package com.ankamagames.wakfu.common.game.item;

import org.apache.log4j.*;

public class Wallet
{
    protected static final Logger m_logger;
    private int m_amountOfCash;
    private WalletEventListener m_listener;
    
    public void setListener(final WalletEventListener listener) {
        this.m_listener = listener;
        if (this.m_listener != null) {
            this.m_listener.onWalletUpdated(this, 0);
        }
    }
    
    public int getAmountOfCash() {
        return this.m_amountOfCash;
    }
    
    private void notifyListener(final int delta) {
        if (this.m_listener != null) {
            this.m_listener.onWalletUpdated(this, delta);
        }
    }
    
    public boolean setAmountOfCash(final int amountOfCash) {
        if (amountOfCash < 0) {
            return false;
        }
        final int delta = amountOfCash - this.m_amountOfCash;
        this.m_amountOfCash = amountOfCash;
        this.notifyListener(delta);
        return true;
    }
    
    public boolean addAmount(final int amountOfCash) {
        return amountOfCash > 0 && this.setAmountOfCash(this.m_amountOfCash + amountOfCash);
    }
    
    public boolean substractAmount(final int amountOfCash) {
        return amountOfCash > 0 && this.setAmountOfCash(Math.max(0, this.m_amountOfCash - amountOfCash));
    }
    
    static {
        m_logger = Logger.getLogger((Class)Wallet.class);
    }
}
