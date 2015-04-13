package com.ankamagames.wakfu.common.game.collector.limited;

import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.item.*;

public class CollectorInventoryCheckerLimited extends CollectorInventoryChecker
{
    public CollectorInventoryCheckerLimited(final IECollectorParameter param) {
        super(param);
    }
    
    @Override
    public boolean canAddCash(final Wallet wallet, final int amountOfCash) {
        final int expectedQty = this.m_param.getCashQty();
        return expectedQty != 0 && wallet.getAmountOfCash() < expectedQty;
    }
    
    @Override
    public boolean canSubCash(final Wallet wallet, final int amountOfCash) {
        throw new UnsupportedOperationException("Impossible de retirer d'argent sur ce type d'inventaire");
    }
    
    public boolean canAddItem(final CollectorInventoryLimited inventory, final int itemRefId, final int qty) {
        final int expectedQty = this.m_param.getQty(itemRefId);
        final int inQuantity = inventory.getQuantity(itemRefId);
        return expectedQty != 0 && qty <= expectedQty - inQuantity;
    }
    
    public int canRemoveItem(final CollectorInventoryLimited inventory, final int itemRefId, final int qty) {
        throw new UnsupportedOperationException("Impossible de retirer d'objet sur ce type d'inventaire");
    }
}
