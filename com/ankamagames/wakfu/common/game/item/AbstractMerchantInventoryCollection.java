package com.ankamagames.wakfu.common.game.item;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.flea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;

public abstract class AbstractMerchantInventoryCollection<IT extends AbstractMerchantInventoryItem, MI extends AbstractMerchantInventory>
{
    protected static final Logger m_logger;
    private final FleaTransactionLog m_transactionLog;
    private final TLongObjectHashMap<MI> m_merchantInventories;
    protected final ArrayList<MerchantInventoryCollectionObserver> m_observers;
    private final InventoryObserver m_contentObserver;
    
    protected AbstractMerchantInventoryCollection() {
        super();
        this.m_transactionLog = new FleaTransactionLog(200, 30, 10, 6);
        this.m_merchantInventories = new TLongObjectHashMap<MI>();
        this.m_observers = new ArrayList<MerchantInventoryCollectionObserver>(1);
        this.m_contentObserver = new InventoryObserver() {
            @Override
            public void onInventoryEvent(final InventoryEvent event) {
                for (final MerchantInventoryCollectionObserver observer : AbstractMerchantInventoryCollection.this.m_observers) {
                    observer.onMerchantInventoryUpdated((AbstractMerchantInventory)event.getInventory(), event);
                }
            }
        };
        this.addObserver(new MerchantInventoryCollectionObserver() {
            @Override
            public void onMerchantInventoryAdded(final AbstractMerchantInventory inventory) {
                inventory.addObserver(AbstractMerchantInventoryCollection.this.m_contentObserver);
            }
            
            @Override
            public void onMerchantInventoryRemoved(final AbstractMerchantInventory inventory) {
                inventory.removeObserver(AbstractMerchantInventoryCollection.this.m_contentObserver);
            }
            
            @Override
            public void onMerchantInventoryUpdated(final AbstractMerchantInventory inventory, final InventoryEvent modification) {
            }
        });
    }
    
    public final FleaTransactionLog getTransactionLog() {
        return this.m_transactionLog;
    }
    
    public final void addObserver(final MerchantInventoryCollectionObserver observer) {
        if (!this.m_observers.contains(observer)) {
            this.m_observers.add(observer);
        }
    }
    
    public final void removeObserver(final MerchantInventoryCollectionObserver observer) {
        this.m_observers.remove(observer);
    }
    
    public final void addMerchantInventory(final MI inventory) {
        this.m_merchantInventories.put(inventory.getUid(), inventory);
        for (final MerchantInventoryCollectionObserver observer : this.m_observers) {
            observer.onMerchantInventoryAdded(inventory);
        }
    }
    
    public final void removeMerchantInventory(final MI inventory) {
        this.removeMerchantInventory(inventory.getUid());
    }
    
    public final void removeMerchantInventory(final long inventoryUid) {
        final MI inventory = this.m_merchantInventories.remove(inventoryUid);
        for (final MerchantInventoryCollectionObserver observer : this.m_observers) {
            observer.onMerchantInventoryRemoved(inventory);
        }
    }
    
    public final void forEachMerchantInventory(final TObjectProcedure<MI> procedure) {
        if (!this.m_merchantInventories.isEmpty()) {
            this.m_merchantInventories.forEachValue(procedure);
        }
    }
    
    public final TLongObjectIterator<MI> getMerchantInventoriesIterator() {
        return this.m_merchantInventories.iterator();
    }
    
    public final int getMerchantInventoriesCount() {
        return this.m_merchantInventories.size();
    }
    
    public final void clear() {
        this.m_transactionLog.clear();
        this.m_merchantInventories.forEachValue(new TObjectProcedure<MI>() {
            @Override
            public boolean execute(final MI inventory) {
                for (final MerchantInventoryCollectionObserver observer : AbstractMerchantInventoryCollection.this.m_observers) {
                    observer.onMerchantInventoryRemoved(inventory);
                }
                return true;
            }
        });
        this.m_merchantInventories.clear();
    }
    
    public final MI getInventoryWithUid(final long merchantInventoryUid) {
        return this.m_merchantInventories.get(merchantInventoryUid);
    }
    
    public final ObjectPair<MI, IT> getItemWithUniqueId(final long itemUniqueId) {
        final TLongObjectIterator<MI> it = this.m_merchantInventories.iterator();
        while (it.hasNext()) {
            it.advance();
            final IT item = (IT)it.value().getWithUniqueId(itemUniqueId);
            if (item != null) {
                return new ObjectPair<MI, IT>(it.value(), item);
            }
        }
        return null;
    }
    
    public final boolean containsItemWithReferenceId(final int itemRefId) {
        final TLongObjectIterator<MI> it = this.m_merchantInventories.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value().containsReferenceId(itemRefId)) {
                return true;
            }
        }
        return false;
    }
    
    public final ObjectPair<MI, IT> removeItemWithUniqueId(final long itemUniqueId) {
        final TLongObjectIterator<MI> it = this.m_merchantInventories.iterator();
        while (it.hasNext()) {
            it.advance();
            final IT item = (IT)it.value().removeWithUniqueId(itemUniqueId);
            if (item != null) {
                return new ObjectPair<MI, IT>(it.value(), item);
            }
        }
        return null;
    }
    
    public final int getItemsCount() {
        int size = 0;
        final TLongObjectIterator<MI> it = this.m_merchantInventories.iterator();
        while (it.hasNext()) {
            it.advance();
            size += it.value().size();
        }
        return size;
    }
    
    public final int getMaximumSize() {
        int size = 0;
        final TLongObjectIterator<MI> it = this.m_merchantInventories.iterator();
        while (it.hasNext()) {
            it.advance();
            size += it.value().getMaximumSize();
        }
        return size;
    }
    
    public final ObjectPair<Long, Short> getItemPositionWithUniqueId(final long uniqueId) {
        final TLongObjectIterator<MI> it = this.m_merchantInventories.iterator();
        while (it.hasNext()) {
            it.advance();
            final short position = it.value().getPosition(uniqueId);
            if (position != -1) {
                return new ObjectPair<Long, Short>(it.value().getUid(), position);
            }
        }
        return null;
    }
    
    public final boolean isLocked() {
        final TLongObjectIterator<MI> it = this.m_merchantInventories.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value().isLocked()) {
                return true;
            }
        }
        return false;
    }
    
    public short getMaxPackSize() {
        final TLongObjectIterator<MI> it = this.m_merchantInventories.iterator();
        short result = 32767;
        while (it.hasNext()) {
            it.advance();
            final short bagMax = it.value().getMaximumPack();
            if (bagMax < result) {
                result = bagMax;
            }
        }
        return result;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractMerchantInventoryCollection.class);
    }
}
