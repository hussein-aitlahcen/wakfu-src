package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics.*;
import java.util.*;
import gnu.trove.*;

final class CosmeticsInventoryModel implements CosmeticsInventory
{
    private final List<CosmeticsInventoryListener> m_listeners;
    private final TIntObjectHashMap<CosmeticsItem> m_items;
    
    CosmeticsInventoryModel() {
        super();
        this.m_listeners = new ArrayList<CosmeticsInventoryListener>();
        this.m_items = new TIntObjectHashMap<CosmeticsItem>();
    }
    
    @Override
    public boolean forEach(final TObjectProcedure<CosmeticsItem> procedure) {
        return this.m_items.forEachValue(procedure);
    }
    
    @Override
    public CosmeticsItem getItem(final int refId) {
        return this.m_items.get(refId);
    }
    
    void add(final CosmeticsItem item) {
        this.m_items.put(item.getRefId(), item);
        this.fireItemAdded(item);
    }
    
    CosmeticsItem remove(final int refId) {
        final CosmeticsItem cosmeticsItem = this.m_items.remove(refId);
        if (cosmeticsItem != null) {
            this.fireItemRemoved(refId);
        }
        return cosmeticsItem;
    }
    
    void clear() {
        this.m_items.clear();
    }
    
    private void fireItemAdded(final CosmeticsItem item) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).itemAdded(item);
        }
    }
    
    private void fireItemRemoved(final int itemId) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).itemRemoved(itemId);
        }
    }
    
    @Override
    public boolean addListener(final CosmeticsInventoryListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final CosmeticsInventoryListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "CosmeticsInventoryModel{m_items=" + this.m_items + '}';
    }
}
