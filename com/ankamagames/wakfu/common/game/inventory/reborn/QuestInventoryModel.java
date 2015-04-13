package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import java.util.*;
import gnu.trove.*;

final class QuestInventoryModel implements QuestInventory, QuestItemListener
{
    private final List<QuestInventoryListener> m_listeners;
    private final TIntObjectHashMap<QuestItem> m_items;
    
    QuestInventoryModel() {
        super();
        this.m_listeners = new ArrayList<QuestInventoryListener>();
        this.m_items = new TIntObjectHashMap<QuestItem>();
    }
    
    @Override
    public boolean forEach(final TObjectProcedure<QuestItem> procedure) {
        return this.m_items.forEachValue(procedure);
    }
    
    @Override
    public QuestItem getItem(final int refId) {
        return this.m_items.get(refId);
    }
    
    void add(final QuestItem item) {
        this.m_items.put(item.getRefId(), item);
        item.addListener(this);
        this.fireItemAdded(item);
    }
    
    void remove(final QuestItem item) {
        this.m_items.remove(item.getRefId());
        item.removeListener(this);
        this.fireItemRemoved(item);
    }
    
    void clear() {
        this.m_items.clear();
    }
    
    private void fireItemAdded(final QuestItem item) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).itemAdded(item);
        }
    }
    
    private void fireItemRemoved(final QuestItem item) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).itemRemoved(item);
        }
    }
    
    @Override
    public void quantityChanged(final QuestItem item, final int delta) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).itemQuantityChanged(item, delta);
        }
    }
    
    @Override
    public boolean addListener(final QuestInventoryListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final QuestInventoryListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "QuestItemInventory{m_items=" + this.m_items.size() + '}';
    }
}
