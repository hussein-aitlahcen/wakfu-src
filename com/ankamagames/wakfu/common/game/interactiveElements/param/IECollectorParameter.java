package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;
import gnu.trove.*;
import java.util.*;

public class IECollectorParameter extends IEParameter
{
    private final short m_capacity;
    private final boolean m_locked;
    private final int m_cashQty;
    private final TIntIntHashMap m_items;
    
    public IECollectorParameter(final int paramId, final int visualId, final short capacity, final boolean locked, final int cashQty) {
        super(paramId, visualId, ChaosInteractiveCategory.NO_CHAOS, 0);
        this.m_items = new TIntIntHashMap();
        this.m_capacity = capacity;
        this.m_locked = locked;
        this.m_cashQty = cashQty;
    }
    
    public short getCapacity() {
        return this.m_capacity;
    }
    
    public boolean isLocked() {
        return this.m_locked;
    }
    
    public void addItem(final int itemId, final int qty) {
        this.m_items.put(itemId, qty);
    }
    
    public TIntIntIterator iterator() {
        return this.m_items.iterator();
    }
    
    public boolean hasExpected() {
        return this.m_cashQty != 0 || !this.m_items.isEmpty();
    }
    
    public int getQty(final int itemID) {
        return this.m_items.get(itemID);
    }
    
    public int getCashQty() {
        return this.m_cashQty;
    }
    
    @Override
    public String toString() {
        return "IECollectParameter{m_capacity=" + this.m_capacity + ", m_locked=" + this.m_locked + ", m_items=" + Arrays.toString(this.m_items.keys()) + '}';
    }
}
