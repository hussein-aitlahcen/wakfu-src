package com.ankamagames.wakfu.common.game.protector;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public class AbstractProtectorMerchantClient<Protector extends ProtectorBase> implements MerchantClient<Item>
{
    protected Protector m_protector;
    private final Wallet wallet;
    
    public AbstractProtectorMerchantClient() {
        super();
        this.wallet = new Wallet();
    }
    
    public void setProtector(final Protector protector) {
        this.m_protector = protector;
    }
    
    public Protector getProtector() {
        return this.m_protector;
    }
    
    @Override
    public Wallet getWallet() {
        return this.wallet;
    }
    
    @Override
    public boolean canStockItem(final Item item) {
        return true;
    }
    
    @Override
    public AbstractBag stockItem(final Item item) {
        return null;
    }
    
    @Override
    public AbstractPartition getTransactionLocalisation() {
        return null;
    }
}
