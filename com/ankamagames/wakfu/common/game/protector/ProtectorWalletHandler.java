package com.ankamagames.wakfu.common.game.protector;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;

public class ProtectorWalletHandler<Protector extends ProtectorBase> extends Wallet
{
    private final TByteObjectHashMap<AbstractProtectorMerchantClient> m_merchantClients;
    
    public ProtectorWalletHandler(final Protector protector, final SimpleObjectFactory<AbstractProtectorMerchantClient<Protector>> merchantFactory, @Nullable final AbstractProtectorWalletContextValidator contextValidator) {
        super();
        this.m_merchantClients = new TByteObjectHashMap<AbstractProtectorMerchantClient>();
        final ProtectorWalletContext[] values = ProtectorWalletContext.values();
        for (int i = 0; i < values.length; ++i) {
            final ProtectorWalletContext context = values[i];
            if (contextValidator == null || contextValidator.isValid(protector, context)) {
                final AbstractProtectorMerchantClient<Protector> merchant = merchantFactory.createNew();
                merchant.setProtector(protector);
                this.m_merchantClients.put(context.idx, merchant);
            }
        }
    }
    
    public boolean handle(final ProtectorWalletContext context) {
        return this.m_merchantClients.contains(context.idx);
    }
    
    public int getAmountOfCash(final ProtectorWalletContext context) {
        final AbstractProtectorMerchantClient client = this.m_merchantClients.get(context.idx);
        return (client == null) ? 0 : client.getWallet().getAmountOfCash();
    }
    
    public boolean setAmountOfCash(final ProtectorWalletContext context, final int amountOfCash) {
        final AbstractProtectorMerchantClient client = this.m_merchantClients.get(context.idx);
        return client != null && client.getWallet().setAmountOfCash(amountOfCash);
    }
    
    public boolean addAmount(final ProtectorWalletContext context, final int amountOfCash) {
        final AbstractProtectorMerchantClient client = this.m_merchantClients.get(context.idx);
        return client != null && client.getWallet().addAmount(amountOfCash);
    }
    
    public boolean substractAmount(final ProtectorWalletContext context, final int amountOfCash) {
        final AbstractProtectorMerchantClient client = this.m_merchantClients.get(context.idx);
        return client != null && client.getWallet().substractAmount(amountOfCash);
    }
    
    public void reset(final int resetAmount) {
        final ProtectorWalletContext[] values = ProtectorWalletContext.values();
        for (int i = 0; i < values.length; ++i) {
            this.setAmountOfCash(values[i], 0);
        }
        this.setAmountOfCash(resetAmount);
    }
    
    public AbstractProtectorMerchantClient getMerchantClient(final ProtectorWalletContext context) {
        return this.m_merchantClients.get(context.idx);
    }
    
    public void prepareWalletPart(final RawProtector.Wallet part) {
        part.cashAmount = this.getAmountOfCash();
        final TByteObjectIterator<AbstractProtectorMerchantClient> it = (TByteObjectIterator<AbstractProtectorMerchantClient>)this.m_merchantClients.iterator();
        while (it.hasNext()) {
            it.advance();
            final RawProtector.Wallet.Content content = new RawProtector.Wallet.Content();
            content.contextId = it.key();
            content.cashAmount = it.value().getWallet().getAmountOfCash();
            part.contexts.add(content);
        }
    }
    
    public void onWalletPartChanged(final RawProtector.Wallet part) {
        for (int i = 0; i < part.contexts.size(); ++i) {
            final RawProtector.Wallet.Content content = part.contexts.get(i);
            final ProtectorWalletContext context = ProtectorWalletContext.fromId(content.contextId);
            this.setAmountOfCash(context, content.cashAmount);
        }
        this.setAmountOfCash(part.cashAmount);
    }
}
