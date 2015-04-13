package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.bag.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;

final class BagInventoryModel implements BagInventory, BagListener
{
    private static final TObjectProcedure<Bag> CLEAR_BAG_PROCEDURE;
    private final List<BagInventoryListener> m_listeners;
    private final TLongObjectHashMap<Bag> m_bags;
    
    BagInventoryModel() {
        super();
        this.m_listeners = new ArrayList<BagInventoryListener>();
        this.m_bags = new TLongObjectHashMap<Bag>();
        this.addListener(new BagInventoryLogger());
    }
    
    @Override
    public boolean forEach(final TObjectProcedure<Bag> procedure) {
        return this.m_bags.forEachValue(procedure);
    }
    
    @Override
    public Bag getBag(final long uid) {
        return this.m_bags.get(uid);
    }
    
    void add(final Bag bag) {
        this.m_bags.put(bag.getId(), bag);
        bag.addListener(this);
        this.fireBagAdded(bag);
    }
    
    void remove(final Bag bag) {
        this.m_bags.remove(bag.getId());
        bag.removeListener(this);
        this.fireBagRemoved(bag);
    }
    
    void clear() {
        this.m_bags.forEachValue(BagInventoryModel.CLEAR_BAG_PROCEDURE);
        this.m_bags.clear();
    }
    
    private void fireBagAdded(final Bag bag) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).bagAdded(bag);
        }
    }
    
    private void fireBagRemoved(final Bag bag) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).bagRemoved(bag);
        }
    }
    
    @Override
    public void itemAdded(final Item item) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).bagItemAdded(item);
        }
    }
    
    @Override
    public void itemRemoved(final Item item) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).bagItemRemoved(item);
        }
    }
    
    @Override
    public boolean addListener(final BagInventoryListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final BagInventoryListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "BagInventoryModel{, m_bags=" + this.m_bags.size() + '}';
    }
    
    static {
        CLEAR_BAG_PROCEDURE = new ClearBagProcedure();
    }
    
    private static class ClearBagProcedure implements TObjectProcedure<Bag>
    {
        @Override
        public boolean execute(final Bag object) {
            ((BagModel)object).clear();
            return true;
        }
    }
}
