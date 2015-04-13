package com.ankamagames.wakfu.common.game.collector.limited;

import com.ankamagames.wakfu.common.game.collector.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import gnu.trove.*;

public abstract class CollectorInventoryLimited extends CollectorInventory<CollectorInventoryCheckerLimited, CollectorInventoryObserverLimited>
{
    protected final TIntIntHashMap m_content;
    private final IECollectorParameter m_param;
    
    protected CollectorInventoryLimited(final IECollectorParameter param) {
        super(new CollectorInventoryCheckerLimited(param));
        this.m_content = new TIntIntHashMap();
        this.m_param = param;
    }
    
    public void notifyItemAdded(final int refId, final int qty) {
        if (this.m_observer != null) {
            ((CollectorInventoryObserverLimited)this.m_observer).onItemAdded(refId, qty);
        }
    }
    
    public boolean add(final int itemRefId, final int qty) {
        if (!this.canAddItem(itemRefId, qty)) {
            return false;
        }
        this.m_content.adjustOrPutValue(itemRefId, qty, qty);
        this.notifyItemAdded(itemRefId, qty);
        return true;
    }
    
    public int getQuantity(final int itemRefId) {
        return this.m_content.get(itemRefId);
    }
    
    @Override
    public boolean isFull() {
        if (this.m_wallet.getAmountOfCash() < this.m_param.getCashQty()) {
            return false;
        }
        final TIntIntIterator it = this.m_param.iterator();
        while (it.hasNext()) {
            it.advance();
            if (this.getQuantity(it.key()) < it.value()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_content.clear();
    }
    
    public boolean canAddItem(final int itemRefId, final int qty) {
        return ((CollectorInventoryCheckerLimited)this.m_checker).canAddItem(this, itemRefId, qty);
    }
    
    public int canRemoveItem(final int refId, final int qty) {
        return ((CollectorInventoryCheckerLimited)this.m_checker).canRemoveItem(this, refId, qty);
    }
}
