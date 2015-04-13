package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.bag.*;

final class BagModel implements Bag, BagItemListener
{
    private final List<BagListener> m_listeners;
    private final TByteObjectHashMap<Item> m_items;
    private final TLongByteHashMap m_itemIds;
    private final long m_id;
    private final AbstractReferenceItem m_refItem;
    
    BagModel(final long id, final AbstractReferenceItem refItem) {
        super();
        this.m_listeners = new ArrayList<BagListener>();
        this.m_items = new TByteObjectHashMap<Item>();
        this.m_itemIds = new TLongByteHashMap();
        this.m_id = id;
        this.m_refItem = refItem;
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public Item getItem(final long uid) {
        return this.m_items.get(this.m_itemIds.get(uid));
    }
    
    @Override
    public Item getItemFromPosition(final byte position) {
        return this.m_items.get(position);
    }
    
    @Override
    public boolean forEach(final TObjectProcedure<Item> procedure) {
        return this.m_items.forEachValue(procedure);
    }
    
    void add(final byte position, final Item item) {
        this.m_items.put(position, item);
        this.m_itemIds.put(item.getUniqueId(), position);
        item.addListener(this);
        this.fireItemAdded(item);
    }
    
    void remove(final Item item) {
        this.m_items.remove(this.m_itemIds.remove(item.getUniqueId()));
        item.removeListener(this);
        this.fireItemRemoved(item);
    }
    
    void clear() {
        this.m_items.clear();
        this.m_itemIds.clear();
    }
    
    @Override
    public BagType getType() {
        return BagType.None;
    }
    
    private void fireItemAdded(final Item item) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).itemAdded(item);
        }
    }
    
    private void fireItemRemoved(final Item item) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).itemRemoved(item);
        }
    }
    
    @Override
    public boolean addListener(final BagListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final BagListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "BagModel{m_id=" + this.m_id + ", m_refItem=" + this.m_refItem + ", m_items=" + this.m_items.size() + '}';
    }
}
