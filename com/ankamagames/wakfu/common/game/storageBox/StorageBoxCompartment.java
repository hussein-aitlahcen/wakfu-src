package com.ankamagames.wakfu.common.game.storageBox;

import com.ankamagames.wakfu.common.game.inventory.action.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.common.rawData.*;

public class StorageBoxCompartment implements ItemInventoryHandler
{
    private static final Logger m_logger;
    private final int m_id;
    private final ArrayInventory<Item, RawInventoryItem> m_inventory;
    
    StorageBoxCompartment(final int id, final byte capacity) {
        super();
        this.m_id = id;
        this.m_inventory = new ArrayInventory<Item, RawInventoryItem>(StorageBoxContentProvider.INSTANCE, StorageBoxContentChecker.INSTANCE, capacity, true);
    }
    
    StorageBoxCompartment(final int id, final ArrayInventory<Item, RawInventoryItem> innerInventory) {
        super();
        this.m_id = id;
        this.m_inventory = innerInventory;
    }
    
    @Override
    public boolean canAdd(final Item item) {
        return this.m_inventory.canAdd(item);
    }
    
    @Override
    public boolean add(final Item item) {
        try {
            return this.canAdd(item) && this.m_inventory.add(item);
        }
        catch (InventoryException e) {
            StorageBoxCompartment.m_logger.error((Object)"[STORAGE_BOX] Impossible d'ajouter un item \u00e0 l'inventaire alors qu'on \u00e0 pourtant test\u00e9 le canAdd", (Throwable)e);
            return false;
        }
    }
    
    @Override
    public boolean canAdd(final Item item, final byte position) {
        return this.m_inventory.canAdd(item, position);
    }
    
    @Override
    public boolean add(final Item item, final byte position) {
        try {
            return this.canAdd(item, position) && this.m_inventory.addAt(item, position);
        }
        catch (InventoryException e) {
            StorageBoxCompartment.m_logger.error((Object)"[STORAGE_BOX] Impossible d'ajouter un item \u00e0 l'inventaire alors qu'on \u00e0 pourtant test\u00e9 le canAdd", (Throwable)e);
            return false;
        }
    }
    
    @Override
    public boolean canRemove(final Item item) {
        return this.m_inventory.canRemove(item);
    }
    
    @Override
    public boolean remove(final Item item) {
        return this.canRemove(item) && this.m_inventory.remove(item);
    }
    
    @Override
    public boolean canRemove(final long itemUid, final short qty) {
        return this.m_inventory.canRemove(itemUid, qty);
    }
    
    @Override
    public boolean remove(final long itemUid, final short qty) {
        return this.canRemove(itemUid, qty) && this.m_inventory.updateQuantity(itemUid, (short)(-qty));
    }
    
    @Override
    public boolean isRemote() {
        return false;
    }
    
    @Override
    public Item getItem(final long uniqueId) {
        return this.m_inventory.getWithUniqueId(uniqueId);
    }
    
    @Override
    public Item getItemFromPosition(final byte position) {
        return this.m_inventory.getFromPosition(position);
    }
    
    public int size() {
        return this.m_inventory.size();
    }
    
    public short getMaximumSize() {
        return this.m_inventory.getMaximumSize();
    }
    
    @Override
    public byte getPosition(final long uniqueId) {
        return (byte)this.m_inventory.getPosition(uniqueId);
    }
    
    public Iterator<Item> iterator() {
        return this.m_inventory.iterator(false);
    }
    
    public Iterator<Item> iteratorWithNulls() {
        return this.m_inventory.iterator(true);
    }
    
    public void addInventoryObserver(final InventoryObserver storageBoxLogEventObserver) {
        this.m_inventory.addObserver(storageBoxLogEventObserver);
    }
    
    public void removeInventoryObserver(final InventoryObserver storageBoxLogEventObserver) {
        this.m_inventory.removeObserver(storageBoxLogEventObserver);
    }
    
    public void toRaw(final RawStorageBoxCompartment rawCompartment) {
        rawCompartment.id = this.m_id;
        for (final Item item : this.m_inventory) {
            final RawStorageBoxCompartment.Item innerItem = new RawStorageBoxCompartment.Item();
            innerItem.position = this.m_inventory.getPosition(item.getUniqueId());
            item.toRaw(innerItem.item);
            rawCompartment.items.add(innerItem);
        }
    }
    
    public void fromRaw(final RawStorageBoxCompartment rawCompartment) {
        this.m_inventory.destroyAll();
        final StorageBoxContentProvider provider = (StorageBoxContentProvider)this.m_inventory.getContentProvider();
        for (int i = 0, size = rawCompartment.items.size(); i < size; ++i) {
            final RawStorageBoxCompartment.Item innerItem = rawCompartment.items.get(i);
            final Item item = provider.unSerializeContent(innerItem.item);
            try {
                this.m_inventory.addAt(item, innerItem.position);
            }
            catch (InventoryException e) {
                StorageBoxCompartment.m_logger.error((Object)"[STORAGE_BOX] Probl\u00e8me de r\u00e9cup\u00e9ration d'un item de compartiment", (Throwable)e);
            }
        }
    }
    
    @Override
    public String toString() {
        return "StorageBoxCompartment{m_inventory=" + this.m_inventory + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)StorageBoxCompartment.class);
    }
}
