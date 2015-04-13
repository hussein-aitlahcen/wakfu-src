package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.temporary.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;
import java.util.*;

class TemporaryInventoryModel implements TemporaryInventory
{
    private static final byte MAX_SIZE = 25;
    private final LinkedList<Item> m_items;
    private final ArrayList<TemporaryInventoryListener> m_listeners;
    
    TemporaryInventoryModel() {
        super();
        this.m_items = new LinkedList<Item>();
        this.m_listeners = new ArrayList<TemporaryInventoryListener>();
    }
    
    @Override
    public Item getByIdx(final short index) {
        return this.m_items.get(index);
    }
    
    @Nullable
    @Override
    public Item getById(final long id) {
        final short index = this.indexOfById(id);
        return (index == -1) ? null : this.m_items.get(index);
    }
    
    @Override
    public boolean containsItemUid(final long uid) {
        return this.indexOfById(uid) != -1;
    }
    
    void add(final Item item) {
        this.m_items.add(item);
        this.fireItemAdded(item);
    }
    
    @Nullable
    private Item removeByIndex(final short index) {
        if (index != -1) {
            final Item removedItem = this.m_items.remove(index);
            this.fireItemRemoved(removedItem);
            return removedItem;
        }
        return null;
    }
    
    Item remove(final long uniqueId) {
        return this.removeByIndex(this.indexOfById(uniqueId));
    }
    
    boolean remove(final Item itemToRemove) {
        return this.remove(itemToRemove.getUniqueId()) != null;
    }
    
    Item removeFirst() {
        if (!this.m_items.isEmpty()) {
            return this.removeByIndex((short)0);
        }
        return null;
    }
    
    void setQuantity(final long itemId, final short quantity) {
        final Item item = this.getById(itemId);
        if (item != null) {
            item.setQuantity(quantity);
            this.fireItemQuantityChanged(item);
        }
    }
    
    short indexOfById(final long itemId) {
        for (int i = 0, size = this.m_items.size(); i < size; ++i) {
            if (this.m_items.get(i).getUniqueId() == itemId) {
                return (short)i;
            }
        }
        return -1;
    }
    
    short size() {
        return (short)this.m_items.size();
    }
    
    private void fireItemAdded(final Item item) {
        for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
            this.m_listeners.get(i).onItemAdded(item);
        }
    }
    
    private void fireItemRemoved(final Item item) {
        for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
            this.m_listeners.get(i).onItemRemoved(item);
        }
    }
    
    private void fireItemQuantityChanged(final Item item) {
        for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
            this.m_listeners.get(i).onItemQuantityChanged(item);
        }
    }
    
    private void fireClear() {
        for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
            this.m_listeners.get(i).onClear();
        }
    }
    
    @Override
    public void addListener(final TemporaryInventoryListener l) {
        this.m_listeners.add(l);
    }
    
    @Override
    public void removeListener(final TemporaryInventoryListener l) {
        this.m_listeners.remove(l);
    }
    
    public void removeAllListeners() {
        this.m_listeners.clear();
    }
    
    @Override
    public boolean isFull() {
        return this.m_items.size() == 25;
    }
    
    @Override
    public boolean isEmpty() {
        return this.m_items.isEmpty();
    }
    
    @Override
    public void clear() {
        this.forEach(ClearProcedure.INSTANCE);
        this.m_items.clear();
        this.fireClear();
    }
    
    public boolean contains(final long itemId) {
        return this.indexOfById(itemId) != -1;
    }
    
    @Override
    public boolean contains(final Item item) {
        return this.contains(item.getUniqueId());
    }
    
    @Override
    public boolean forEach(final TObjectProcedure<Item> procedure) {
        for (int i = 0, size = this.m_items.size(); i < size; ++i) {
            final Item item = this.m_items.get(i);
            if (!procedure.execute(item)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TemporaryInventoryModel");
        sb.append("{m_items=").append((this.m_items == null) ? "null" : Collections.singletonList(this.m_items).toString());
        sb.append('}');
        return sb.toString();
    }
    
    private static final class ClearProcedure implements TObjectProcedure<Item>
    {
        static final ClearProcedure INSTANCE;
        
        @Override
        public boolean execute(final Item object) {
            object.release();
            return false;
        }
        
        static {
            INSTANCE = new ClearProcedure();
        }
    }
}
