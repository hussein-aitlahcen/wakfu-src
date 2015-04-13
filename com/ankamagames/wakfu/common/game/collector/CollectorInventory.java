package com.ankamagames.wakfu.common.game.collector;

import com.ankamagames.wakfu.common.game.collector.limited.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;

public abstract class CollectorInventory<Checker extends CollectorInventoryChecker, Observer extends CollectorInventoryObserver>
{
    protected static final Logger m_logger;
    @Deprecated
    protected final Checker m_checker;
    protected Observer m_observer;
    protected final Wallet m_wallet;
    
    protected CollectorInventory(final Checker checker) {
        super();
        this.m_wallet = new Wallet();
        this.m_checker = checker;
    }
    
    public int getAmountOfCash() {
        return this.m_wallet.getAmountOfCash();
    }
    
    public boolean addAmount(final int amountOfCash) {
        return this.canAddCash(amountOfCash) && this.m_wallet.addAmount(amountOfCash);
    }
    
    public boolean subtractAmount(final int amountOfCash) {
        return this.canSubtractCash(amountOfCash) && this.m_wallet.substractAmount(amountOfCash);
    }
    
    public void setObserver(final Observer o) {
        if (this.m_observer != null) {
            throw new UnsupportedOperationException("Listener already SET");
        }
        this.m_observer = o;
        this.m_wallet.setListener(o);
    }
    
    public void removeObserver() {
        this.m_observer = null;
        this.m_wallet.setListener(null);
    }
    
    public boolean canAddCash(final int amountOfCash) {
        return this.m_checker.canAddCash(this.m_wallet, amountOfCash);
    }
    
    public boolean canSubtractCash(final int amountOfCash) {
        return this.m_checker.canSubCash(this.m_wallet, amountOfCash);
    }
    
    public void clear() {
        this.m_wallet.setAmountOfCash(0);
    }
    
    public abstract boolean isFull();
    
    static {
        m_logger = Logger.getLogger((Class)CollectorInventory.class);
    }
}
