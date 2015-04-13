package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;

public abstract class StackInventory<C extends InventoryContent, R> extends Inventory<C>
{
    private static final Logger m_logger;
    protected final HashMap<Long, C> m_contents;
    protected final InventoryContentProvider<C, R> m_contentProvider;
    protected InventoryContentChecker<C> m_contentChecker;
    protected final boolean m_ordered;
    protected final boolean m_serializeQuantity;
    
    public StackInventory(final short maximumSize, final InventoryContentProvider<C, R> contentProvider, final InventoryContentChecker<C> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        super(stackable, maximumSize);
        this.m_contents = (ordered ? new LinkedHashMap<Long, C>() : new HashMap<Long, C>());
        this.setMaximumSize(maximumSize);
        this.m_contentProvider = contentProvider;
        this.m_contentChecker = contentChecker;
        this.m_ordered = ordered;
        this.m_serializeQuantity = serializeQuantity;
    }
    
    public InventoryContentProvider<C, R> getContentProvider() {
        return this.m_contentProvider;
    }
    
    @Override
    public boolean add(final C item) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        if (item == null) {
            return false;
        }
        if (item.getQuantity() <= 0) {
            StackInventory.m_logger.warn((Object)("Impossile d'ajouter un item avec un quantit\u00e9e de " + item.getQuantity()));
            return false;
        }
        if (this.m_stacking) {
            short remainingQuantity = item.getQuantity();
            for (final C content : this.m_contents.values()) {
                if (item.canStackWith(content)) {
                    final int emptyPlace = content.getStackMaximumHeight() - content.getQuantity();
                    if (emptyPlace <= 0) {
                        continue;
                    }
                    final short quantityToStack = (short)((remainingQuantity < emptyPlace) ? remainingQuantity : emptyPlace);
                    remainingQuantity -= quantityToStack;
                    content.updateQuantity(quantityToStack);
                    this.notifyObservers(InventoryItemModifiedEvent.checkOutQuantityEvent(this, content, quantityToStack));
                    if (remainingQuantity <= 0) {
                        break;
                    }
                    continue;
                }
            }
            C newContent = item;
            while (remainingQuantity > 0) {
                final short qtyToPut = (remainingQuantity < item.getStackMaximumHeight()) ? remainingQuantity : item.getStackMaximumHeight();
                newContent.setQuantity(qtyToPut);
                remainingQuantity -= qtyToPut;
                if (this.m_contentChecker != null && this.m_contentChecker.canAddItem(this, newContent) < 0) {
                    return false;
                }
                this.m_contents.put(newContent.getUniqueId(), newContent);
                this.notifyObservers(InventoryItemModifiedEvent.checkOutAddEvent(this, newContent));
                if (remainingQuantity <= 0) {
                    continue;
                }
                newContent = (C)newContent.getCopy(false);
            }
            return true;
        }
        if (this.isFull()) {
            throw new InventoryCapacityReachedException("Cannot add item : maximum size of inventory is reached (" + this.getMaximumSize() + ')');
        }
        if (this.m_contents.containsKey(item.getUniqueId())) {
            throw new ContentAlreadyPresentException("Item with uniqueID " + item.getUniqueId() + " is already present in the inventory", item, this.m_contents.get(item.getUniqueId()));
        }
        if (this.m_contentChecker != null && this.m_contentChecker.canAddItem(this, item) < 0) {
            return false;
        }
        this.m_contents.put(item.getUniqueId(), item);
        this.notifyObservers(InventoryItemModifiedEvent.checkOutAddEvent(this, item));
        return true;
    }
    
    @Override
    public boolean updateQuantity(final long uniqueId, final short quantityUpdate) {
        final C item = this.getWithUniqueId(uniqueId);
        if (item == null) {
            return false;
        }
        if (item.getQuantity() + quantityUpdate <= 0) {
            return this.destroy(item);
        }
        item.updateQuantity(quantityUpdate);
        this.notifyObservers(InventoryItemModifiedEvent.checkOutQuantityEvent(this, item, quantityUpdate));
        return true;
    }
    
    @Override
    public short getQuantity(final long uniqueId) {
        final C item = this.getWithUniqueId(uniqueId);
        if (item == null) {
            return 0;
        }
        return item.getQuantity();
    }
    
    @Override
    public boolean remove(final C item) {
        if (item == null) {
            return false;
        }
        if (this.m_contentChecker != null && this.m_contentChecker.canRemoveItem(this, item) < 0) {
            return false;
        }
        if (this.m_contents.remove(item.getUniqueId()) == null) {
            return false;
        }
        this.notifyObservers(InventoryItemModifiedEvent.checkOutRemoveEvent(this, item));
        return true;
    }
    
    @Override
    public boolean destroy(final C item) {
        if (item == null) {
            return false;
        }
        if (this.m_contentChecker != null && this.m_contentChecker.canRemoveItem(this, item) < 0) {
            return false;
        }
        if (this.m_contents.remove(item.getUniqueId()) == null) {
            return false;
        }
        this.notifyObservers(InventoryItemModifiedEvent.checkOutRemoveEvent(this, item));
        item.release();
        return true;
    }
    
    @Nullable
    @Override
    public C removeWithUniqueId(final long itemUniqueId) {
        final C item = this.m_contents.get(itemUniqueId);
        if (item == null) {
            return null;
        }
        if (this.m_contentChecker != null && this.m_contentChecker.canRemoveItem(this, item) < 0) {
            return null;
        }
        this.m_contents.remove(itemUniqueId);
        this.notifyObservers(InventoryItemModifiedEvent.checkOutRemoveEvent(this, item));
        return item;
    }
    
    @Override
    public Iterator<C> iterator() {
        return this.m_contents.values().iterator();
    }
    
    @Override
    public boolean contains(final C item) {
        return item != null && this.m_contents.containsKey(item.getUniqueId());
    }
    
    @Override
    public boolean containsUniqueId(final long uniqueId) {
        return this.m_contents.containsKey(uniqueId);
    }
    
    @Override
    public boolean containsReferenceId(final int referenceId) {
        for (final C item : this.m_contents.values()) {
            if (item.getReferenceId() == referenceId) {
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    @Override
    public C getWithUniqueId(final long uniqueId) {
        return this.m_contents.get(uniqueId);
    }
    
    @Nullable
    @Override
    public C getFirstWithReferenceId(final int referenceId) {
        for (final C item : this.m_contents.values()) {
            if (item.getReferenceId() == referenceId) {
                return item;
            }
        }
        return null;
    }
    
    @Override
    public C getFirstWithReferenceId(final int referenceId, final InventoryContentValidator<C> validator) {
        for (final C item : this.m_contents.values()) {
            if (item.getReferenceId() == referenceId && validator.isValid(item)) {
                return item;
            }
        }
        return null;
    }
    
    @Override
    public ArrayList<C> getAllWithReferenceId(final int referenceId) {
        final ArrayList<C> items = new ArrayList<C>();
        for (final C item : this.m_contents.values()) {
            if (item.getReferenceId() == referenceId || referenceId == -1) {
                items.add(item);
            }
        }
        return items;
    }
    
    @Override
    public ArrayList<C> getAllWithReferenceId(final int referenceId, final InventoryContentValidator<C> validator) {
        final ArrayList<C> items = new ArrayList<C>();
        for (final C item : this.m_contents.values()) {
            if ((item.getReferenceId() == referenceId || referenceId == -1) && validator.isValid(item)) {
                items.add(item);
            }
        }
        return items;
    }
    
    @Override
    public ArrayList<C> getAllWithValidator(final InventoryContentValidator<C> validator) {
        final ArrayList<C> items = new ArrayList<C>();
        for (final C item : this.m_contents.values()) {
            if (item != null && validator.isValid(item)) {
                items.add(item);
            }
        }
        return items;
    }
    
    @Override
    public int size() {
        return this.m_contents.size();
    }
    
    @Override
    public int removeAll() {
        final int itemsCount = this.size();
        this.m_contents.clear();
        if (itemsCount > 0) {
            this.notifyObservers(InventoryClearedEvent.checkOut(this));
        }
        return itemsCount;
    }
    
    @Override
    public int destroyAll() {
        final int itemsCount = this.size();
        for (final C item : this.m_contents.values()) {
            item.release();
        }
        this.m_contents.clear();
        if (itemsCount > 0) {
            this.notifyObservers(InventoryClearedEvent.checkOut(this));
        }
        return itemsCount;
    }
    
    @Override
    public InventoryContentChecker<C> getContentChecker() {
        return this.m_contentChecker;
    }
    
    @Override
    public void setContentChecker(final InventoryContentChecker<C> checker) {
        this.m_contentChecker = checker;
    }
    
    @Override
    public String toString() {
        final StringBuilder res = new StringBuilder("StackInventory = ");
        for (final C c : this.m_contents.values()) {
            res.append(c.getUniqueId()).append(' ');
        }
        return res.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)StackInventory.class);
    }
}
