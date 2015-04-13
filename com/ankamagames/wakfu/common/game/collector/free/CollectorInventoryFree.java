package com.ankamagames.wakfu.common.game.collector.free;

import com.ankamagames.wakfu.common.game.collector.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.collector.free.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public abstract class CollectorInventoryFree extends CollectorInventory<CollectorInventoryCheckerFree, CollectorInventoryObserverFree>
{
    protected final ContiguousArrayInventory<Item, RawInventoryItem> m_content;
    
    protected CollectorInventoryFree(final IECollectorParameter param) {
        super(new CollectorInventoryCheckerFree(param));
        this.m_content = new ContiguousArrayInventory<Item, RawInventoryItem>(CollectorStackInventoryProvider.INSTANCE, (InventoryContentChecker<Item>)this.m_checker, param.getCapacity(), true);
    }
    
    public void notifyItemAdded(final Item item) {
        if (this.m_observer != null) {
            ((CollectorInventoryObserverFree)this.m_observer).onItemAdded(item);
        }
    }
    
    public boolean add(final Item item) {
        try {
            if (!this.canAdd(item)) {
                return false;
            }
            this.m_content.add(item);
        }
        catch (InventoryException e) {
            CollectorInventoryFree.m_logger.error((Object)"Impossible d'ajouter un item \u00e0 l'inventaire alors qu'on \u00e0 pourtant test\u00e9 le canAdd", (Throwable)e);
            return false;
        }
        this.notifyItemAdded(item);
        return true;
    }
    
    public boolean add(final Item item, final byte position) {
        return false;
    }
    
    public boolean remove(final long itemUid, final short qty) {
        if (!this.canRemove(itemUid, qty)) {
            return false;
        }
        this.m_content.updateQuantity(itemUid, (short)(-qty));
        return true;
    }
    
    public boolean remove(final Item item) {
        if (!this.canRemove(item)) {
            return false;
        }
        this.m_content.remove(item);
        return true;
    }
    
    @Nullable
    public Item getItem(final long uniqueId) {
        return this.m_content.getWithUniqueId(uniqueId);
    }
    
    @Nullable
    public Item getItemFromPosition(final byte position) {
        return this.m_content.getFromPosition(position);
    }
    
    public byte getPosition(final long uniqueId) {
        return (byte)this.m_content.getPosition(uniqueId);
    }
    
    @Override
    public boolean isFull() {
        return this.m_content.isFull();
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_content.cleanup();
    }
    
    public boolean canAdd(final Item item) {
        return ((CollectorInventoryCheckerFree)this.m_checker).canAddItem(this.m_content, item) >= 0;
    }
    
    public boolean canRemove(final long itemUid, final short qty) {
        return ((CollectorInventoryCheckerFree)this.m_checker).canRemoveItem((Inventory<Item>)this.m_content, itemUid, qty) >= 0;
    }
    
    public boolean canRemove(final Item item) {
        return ((CollectorInventoryCheckerFree)this.m_checker).canRemoveItem(this.m_content, item) >= 0;
    }
    
    public boolean canAdd(final Item item, final byte position) {
        return ((CollectorInventoryCheckerFree)this.m_checker).canAddItem(this.m_content, item, position) >= 0;
    }
}
