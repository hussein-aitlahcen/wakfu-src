package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import gnu.trove.*;

public class ArrayInventory<C extends InventoryContent, R> extends Inventory<C> implements LoggableEntity
{
    private static final Logger m_logger;
    protected C[] m_contents;
    protected final TLongShortHashMap m_idxByUniqueId;
    protected final InventoryContentProvider<C, R> m_contentProvider;
    protected InventoryContentChecker<C> m_contentChecker;
    
    public ArrayInventory(final InventoryContentProvider<C, R> contentProvider, final InventoryContentChecker<C> contentChecker, final short maximumSize, final boolean stackable) {
        super(stackable, maximumSize);
        this.m_contents = new InventoryContent[maximumSize];
        this.m_idxByUniqueId = new TLongShortHashMap(maximumSize);
        this.m_contentProvider = contentProvider;
        this.m_contentChecker = contentChecker;
    }
    
    @Override
    public boolean setMaximumSize(final short maxSize) {
        if (maxSize < this.m_maximumSize) {
            ArrayInventory.m_logger.error((Object)"Can't decrease the size of an ArrayInventory");
            return false;
        }
        if (this.m_contents != null && maxSize == this.m_maximumSize) {
            return true;
        }
        if (!super.setMaximumSize(maxSize)) {
            return false;
        }
        final C[] tmp = (C[])new InventoryContent[maxSize];
        System.arraycopy(this.m_contents, 0, tmp, 0, this.m_contents.length);
        this.m_contents = tmp;
        this.m_idxByUniqueId.ensureCapacity(maxSize);
        return true;
    }
    
    @Override
    public boolean add(final C item) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        if (item == null) {
            return false;
        }
        if (item.getQuantity() <= 0) {
            ArrayInventory.m_logger.error((Object)("On essaye d'ajouter un item avec une quantit\u00e9 de " + item.getQuantity()), (Throwable)new Exception());
            return false;
        }
        if (this.m_idxByUniqueId.containsKey(item.getUniqueId())) {
            throw new ContentAlreadyPresentException("Item with uniqueID " + item.getUniqueId() + " is already present in the inventory");
        }
        if (this.m_contentChecker != null && this.m_contentChecker.canAddItem(this, item) < 0) {
            return false;
        }
        short addToIdx = -1;
        short newIdx = -1;
        for (short i = 0, size = (short)this.m_contents.length; i < size; ++i) {
            if (this.m_contents[i] != null && item.canStackWith(this.m_contents[i])) {
                boolean takeNewStackPlace = item.getStackMaximumHeight() > 1;
                if (addToIdx != -1 && this.m_contents[addToIdx].getQuantity() < this.m_contents[i].getQuantity()) {
                    takeNewStackPlace = false;
                }
                if (takeNewStackPlace) {
                    addToIdx = i;
                }
            }
            if (this.m_contents[i] == null && newIdx == -1) {
                newIdx = i;
            }
        }
        if (this.isFull() && addToIdx == -1) {
            throw new InventoryCapacityReachedException("Cannot add item : maximum size of inventory is reached (" + this.getMaximumSize() + ')');
        }
        if (addToIdx >= 0 && item.getQuantity() + this.m_contents[addToIdx].getQuantity() > item.getStackMaximumHeight() && newIdx == -1) {
            throw new InventoryCapacityReachedException("Cannot add item : There is a possible stack, but his maxSize will be reached, and there is no free place for the rest.  MaxSize : " + this.getMaximumSize());
        }
        short qty = 0;
        if (addToIdx != -1) {
            final int a = this.m_contents[addToIdx].getStackMaximumHeight() - this.m_contents[addToIdx].getQuantity();
            final short b = item.getQuantity();
            qty = (short)((a < b) ? a : b);
            this.m_contents[addToIdx].updateQuantity(qty);
            this.notifyObservers(InventoryItemModifiedEvent.checkOutQuantityEvent(this, this.m_contents[addToIdx], addToIdx, qty));
        }
        if (item.getQuantity() - qty > 0) {
            item.updateQuantity((short)(-qty));
            if (item.getQuantity() > 0 && newIdx != -1) {
                this.m_contents[newIdx] = item;
                this.m_idxByUniqueId.put(item.getUniqueId(), newIdx);
                this.notifyObservers(InventoryItemModifiedEvent.checkOutAddEvent(this, item, newIdx));
            }
        }
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
    
    public boolean addAt(final C item, final short position) throws InventoryCapacityReachedException, ContentAlreadyPresentException, PositionAlreadyUsedException {
        if (item == null) {
            ArrayInventory.m_logger.info((Object)"Impossible d'ajouter un item null");
            return false;
        }
        if (position < 0 || position >= this.m_maximumSize) {
            ArrayInventory.m_logger.info((Object)("Impossible d'ajouter un item : position en dehors des limites : " + position), (Throwable)new Exception());
            return false;
        }
        if (this.m_contentChecker != null && this.m_contentChecker.canAddItem(this, item, position) < 0) {
            ArrayInventory.m_logger.info((Object)"Position refus\u00e9e par le checker");
            return false;
        }
        if (this.m_idxByUniqueId.containsKey(item.getUniqueId())) {
            throw new ContentAlreadyPresentException("Item with uniqueID " + item.getUniqueId() + " is already present in the inventory");
        }
        final C stack = (C)this.m_contents[position];
        if (stack == null) {
            this.m_contents[position] = item;
            this.m_idxByUniqueId.put(item.getUniqueId(), position);
            this.notifyObservers(InventoryItemModifiedEvent.checkOutAddEvent(this, item, position));
            return true;
        }
        if (!item.canStackWith(stack)) {
            throw new PositionAlreadyUsedException("Cannot add item " + item + " at position " + position + " item " + this.m_contents[position] + "already present");
        }
        if (stack.getStackMaximumHeight() - stack.getQuantity() < item.getQuantity()) {
            throw new PositionAlreadyUsedException("Cannot add item " + item + " at position " + position + " item " + this.m_contents[position] + "already present");
        }
        return this.updateQuantity(stack.getUniqueId(), item.getQuantity());
    }
    
    @Override
    public boolean remove(final C item) {
        if (item == null) {
            return false;
        }
        if (!this.m_idxByUniqueId.contains(item.getUniqueId())) {
            return false;
        }
        final short idx = this.m_idxByUniqueId.get(item.getUniqueId());
        if (this.m_contents[idx] == null || this.m_contents[idx].getUniqueId() != item.getUniqueId()) {
            ArrayInventory.m_logger.error((Object)("Probl\u00e8me de logique : table d'index et tableau incoh\u00e9rents. Item attendu \u00e0 la position " + idx + " : " + item + " item trouv\u00e9 : " + this.m_contents[idx]));
            return false;
        }
        if (!this.quickRemove(item)) {
            return false;
        }
        this.notifyObservers(InventoryItemModifiedEvent.checkOutRemoveEvent(this, item, idx));
        return true;
    }
    
    @Override
    public boolean destroy(final C item) {
        if (item == null) {
            return false;
        }
        if (!this.m_idxByUniqueId.contains(item.getUniqueId())) {
            return false;
        }
        final short idx = this.m_idxByUniqueId.get(item.getUniqueId());
        if (this.m_contents[idx] == null || this.m_contents[idx].getUniqueId() != item.getUniqueId()) {
            ArrayInventory.m_logger.error((Object)("Probl\u00e8me de logique : table d'index et tableau incoh\u00e9rents. Item attendu \u00e0 la position " + idx + " : " + item + " item trouv\u00e9 : " + this.m_contents[idx]));
            return false;
        }
        if (!this.quickRemove(item)) {
            return false;
        }
        this.notifyObservers(InventoryItemModifiedEvent.checkOutRemoveEvent(this, item, idx));
        item.release();
        return true;
    }
    
    @Nullable
    public C removeAt(final short position) {
        final C item = (C)this.m_contents[position];
        if (item == null) {
            return null;
        }
        if (!this.quickRemove(item)) {
            return null;
        }
        this.notifyObservers(InventoryItemModifiedEvent.checkOutRemoveEvent(this, item, position));
        return item;
    }
    
    public boolean destroyAt(final short position) {
        if (position < 0 || position >= this.m_maximumSize) {
            return false;
        }
        final C item = (C)this.m_contents[position];
        if (item == null) {
            return false;
        }
        if (!this.quickRemove(item)) {
            return false;
        }
        this.notifyObservers(InventoryItemModifiedEvent.checkOutRemoveEvent(this, item, position));
        item.release();
        return true;
    }
    
    public short getPosition(final long uniqueId) {
        if (!this.m_idxByUniqueId.containsKey(uniqueId)) {
            return -1;
        }
        return this.m_idxByUniqueId.get(uniqueId);
    }
    
    @Deprecated
    public short getPosition(final C item) {
        if (item == null) {
            return -1;
        }
        return this.getPosition(item.getUniqueId());
    }
    
    @Nullable
    @Override
    public C removeWithUniqueId(final long itemUniqueId) {
        if (!this.m_idxByUniqueId.contains(itemUniqueId)) {
            return null;
        }
        final short idx = this.m_idxByUniqueId.remove(itemUniqueId);
        if (this.m_contents[idx] == null || this.m_contents[idx].getUniqueId() != itemUniqueId) {
            ArrayInventory.m_logger.error((Object)("Probl\u00e8me de logique : table d'index et tableau incoh\u00e9rents. Item attendu \u00e0 la position " + idx + " : id " + itemUniqueId + ". item trouv\u00e9 : " + this.m_contents[idx] + ((this.m_contents[idx] == null) ? "" : ("(id : " + this.m_contents[idx].getUniqueId() + ')'))));
            this.m_contents[idx] = null;
            return null;
        }
        final C contentToReturn = (C)this.m_contents[idx];
        if (this.m_contentChecker != null && this.m_contentChecker.canRemoveItem(this, contentToReturn) < 0) {
            return null;
        }
        this.m_contents[idx] = null;
        this.notifyObservers(InventoryItemModifiedEvent.checkOutRemoveEvent(this, contentToReturn, idx));
        return contentToReturn;
    }
    
    public void destroyItems(final InventoryContentValidator<C> validator) {
        for (final C content : this.m_contents) {
            if (content != null && (validator == null || validator.isValid(content))) {
                this.destroy(content);
            }
        }
    }
    
    @Override
    public int destroyWithReferenceId(final int referenceId) {
        int removed = 0;
        for (final C content : this.m_contents) {
            if (content != null && content.getReferenceId() == referenceId) {
                removed += content.getQuantity();
                this.destroy(content);
            }
        }
        return removed;
    }
    
    @Override
    public int destroyWithReferenceId(final int referenceId, final int count) {
        int removed = 0;
        for (final C content : this.m_contents) {
            if (content != null && content.getReferenceId() == referenceId) {
                final short quantity = content.getQuantity();
                if (quantity > count - removed) {
                    content.updateQuantity((short)(removed - count));
                    return count;
                }
                if (this.destroy(content)) {
                    removed += quantity;
                }
            }
            if (removed >= count) {
                return removed;
            }
        }
        return removed;
    }
    
    @Override
    public Iterator<C> iterator() {
        return this.iterator(false);
    }
    
    public Iterator<C> iterator(final boolean returnsNull) {
        return new ArrayIterator<C>((C[])this.m_contents, returnsNull);
    }
    
    public C[] toArray(@NotNull final C... array) {
        System.arraycopy(this.m_contents, 0, array, 0, this.m_contents.length);
        return array;
    }
    
    @Override
    public boolean contains(final C item) {
        return item != null && this.m_idxByUniqueId.containsKey(item.getUniqueId());
    }
    
    @Override
    public boolean containsUniqueId(final long uniqueId) {
        return this.m_idxByUniqueId.containsKey(uniqueId);
    }
    
    @Override
    public boolean containsReferenceId(final int referenceId) {
        for (final C item : this.m_contents) {
            if (item != null && item.getReferenceId() == referenceId) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isPositionFree(final short position) {
        return position >= 0 && position < this.m_maximumSize && this.m_contents[position] == null;
    }
    
    @Nullable
    public C getFromPosition(final short position) {
        if (position < 0 || position >= this.m_maximumSize) {
            return null;
        }
        return (C)this.m_contents[position];
    }
    
    @Nullable
    @Override
    public C getWithUniqueId(final long uniqueId) {
        if (!this.m_idxByUniqueId.contains(uniqueId)) {
            return null;
        }
        final short idx = this.m_idxByUniqueId.get(uniqueId);
        return (C)this.m_contents[idx];
    }
    
    @Nullable
    @Override
    public C getFirstWithReferenceId(final int referenceId) {
        for (final C item : this.m_contents) {
            if (item != null && item.getReferenceId() == referenceId) {
                return item;
            }
        }
        return null;
    }
    
    @Nullable
    @Override
    public C getFirstWithReferenceId(final int referenceId, final InventoryContentValidator<C> validator) {
        for (final C item : this.m_contents) {
            if (item != null && item.getReferenceId() == referenceId && validator.isValid(item)) {
                return item;
            }
        }
        return null;
    }
    
    public short getFirstStackableIndexForContent(final C content) {
        short i = 0;
        for (final C item : this.m_contents) {
            if (content.canStackWith(item)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public short getFirstFreeIndex() {
        short i = 0;
        for (final C item : this.m_contents) {
            if (item == null) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    @Override
    public ArrayList<C> getAllWithReferenceId(final int referenceId) {
        final ArrayList<C> items = new ArrayList<C>();
        for (final C item : this.m_contents) {
            if (item != null && item.getReferenceId() == referenceId) {
                items.add(item);
            }
        }
        return items;
    }
    
    @Override
    public ArrayList<C> getAllWithReferenceId(final int referenceId, final InventoryContentValidator<C> validator) {
        final ArrayList<C> items = new ArrayList<C>();
        for (final C item : this.m_contents) {
            if (item != null && item.getReferenceId() == referenceId && validator.isValid(item)) {
                items.add(item);
            }
        }
        return items;
    }
    
    @Override
    public ArrayList<C> getAllWithValidator(final InventoryContentValidator<C> validator) {
        final ArrayList<C> items = new ArrayList<C>();
        for (final C item : this.m_contents) {
            if (item != null && validator.isValid(item)) {
                items.add(item);
            }
        }
        return items;
    }
    
    @Override
    public int size() {
        return this.m_idxByUniqueId.size();
    }
    
    @Override
    public int removeAll() {
        final int itemsCount = this.size();
        if (itemsCount == 0) {
            return 0;
        }
        for (int i = this.m_contents.length - 1; i >= 0; --i) {
            this.m_contents[i] = null;
        }
        this.m_idxByUniqueId.clear();
        this.notifyObservers(InventoryClearedEvent.checkOut(this));
        return itemsCount;
    }
    
    @Override
    public int destroyAll() {
        final int itemsCount = this.size();
        if (itemsCount == 0) {
            return 0;
        }
        for (int i = this.m_contents.length - 1; i >= 0; --i) {
            if (this.m_contents[i] != null) {
                this.m_contents[i].release();
            }
            this.m_contents[i] = null;
        }
        this.m_idxByUniqueId.clear();
        this.notifyObservers(InventoryClearedEvent.checkOut(this));
        return itemsCount;
    }
    
    @Override
    public InventoryContentChecker<C> getContentChecker() {
        return this.m_contentChecker;
    }
    
    @Override
    public void setContentChecker(@NotNull final InventoryContentChecker<C> checker) {
        this.m_contentChecker = checker;
    }
    
    public boolean canAdd(final C item) {
        if (item == null) {
            return false;
        }
        if (item.getQuantity() <= 0) {
            ArrayInventory.m_logger.error((Object)("On essaye de simuler l'ajout d'un item avec une quantit\u00e9 de " + item.getQuantity()), (Throwable)new Exception());
            return false;
        }
        if (this.getWithUniqueId(item.getUniqueId()) != null) {
            ArrayInventory.m_logger.error((Object)("Simulation : Item with uniqueID " + item.getUniqueId() + " is already present in the inventory"), (Throwable)new ContentAlreadyPresentException());
            return false;
        }
        if (this.m_contentChecker != null && this.m_contentChecker.canAddItem(this, item) < 0) {
            return false;
        }
        if (!this.isFull()) {
            return true;
        }
        if (!this.m_stacking) {
            return false;
        }
        short quantity = item.getQuantity();
        for (int i = 0, size = this.m_contents.length; i < size && quantity > 0; ++i) {
            final C content = (C)this.m_contents[i];
            if (item.canStackWith(content)) {
                final int allowed = content.getStackMaximumHeight() - content.getQuantity();
                quantity -= (short)allowed;
            }
        }
        return quantity <= 0;
    }
    
    public boolean canAdd(final C item, final short position) {
        if (item == null) {
            return false;
        }
        if (item.getQuantity() <= 0) {
            ArrayInventory.m_logger.error((Object)("On essaye de simuler l'ajout d'un item avec une quantit\u00e9 de " + item.getQuantity()), (Throwable)new Exception());
            return false;
        }
        if (this.getWithUniqueId(item.getUniqueId()) != null) {
            ArrayInventory.m_logger.error((Object)("Simulation : Item with uniqueID " + item.getUniqueId() + " is already present in the inventory"), (Throwable)new ContentAlreadyPresentException());
            return false;
        }
        if (position < 0 || position > this.m_maximumSize - 1) {
            return false;
        }
        if (this.m_contentChecker != null && this.m_contentChecker.canAddItem(this, item, position) < 0) {
            return false;
        }
        final C stack = (C)this.m_contents[position];
        if (stack == null) {
            return true;
        }
        final boolean enoughFreeSpace = stack.getStackMaximumHeight() - stack.getQuantity() >= item.getQuantity();
        return this.m_stacking && item.canStackWith(stack) && enoughFreeSpace;
    }
    
    public boolean canRemove(final C item) {
        return item != null && !this.isEmpty() && this.containsUniqueId(item.getUniqueId()) && this.contains(item) && (this.m_contentChecker == null || this.m_contentChecker.canRemoveItem(this, item) >= 0);
    }
    
    public boolean canRemove(final long contentUid, final short qty) {
        final C content = this.getWithUniqueId(contentUid);
        return content != null && qty <= content.getQuantity() && (this.m_contentChecker == null || this.m_contentChecker.canRemoveItem(this, content) >= 0);
    }
    
    @Deprecated
    public boolean simulateAddWithRemove(final C item) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        if (item == null) {
            return false;
        }
        if (item.getQuantity() <= 0) {
            ArrayInventory.m_logger.error((Object)("On essaye de simuler l'ajout d'un item avec une quantit\u00e9 de " + item.getQuantity()), (Throwable)new Exception());
            return false;
        }
        if (this.m_idxByUniqueId.containsKey(item.getUniqueId())) {
            throw new ContentAlreadyPresentException("Simulation : Item with uniqueID " + item.getUniqueId() + " is already present in the inventory");
        }
        if (this.m_contentChecker != null && this.m_contentChecker.canAddItem(this, item) < 0) {
            return false;
        }
        short addToIdx = -1;
        short newIdx = -1;
        for (short i = 0, size = (short)this.m_contents.length; i < size; ++i) {
            if (this.m_contents[i] != null && this.m_contents[i] != null && item.canStackWith(this.m_contents[i])) {
                boolean takeNewStackPlace = true;
                if (addToIdx != -1 && this.m_contents[addToIdx].getQuantity() < this.m_contents[i].getQuantity()) {
                    takeNewStackPlace = false;
                }
                if (takeNewStackPlace) {
                    addToIdx = i;
                }
            }
            if (this.m_contents[i] == null && newIdx == -1) {
                newIdx = i;
            }
        }
        if (this.isFull() && addToIdx == -1) {
            return false;
        }
        if (addToIdx >= 0 && item.getQuantity() + this.m_contents[addToIdx].getQuantity() > item.getStackMaximumHeight() && newIdx == -1) {
            return false;
        }
        short qty = 0;
        short oldQty = -1;
        if (addToIdx != -1) {
            final int a = this.m_contents[addToIdx].getStackMaximumHeight() - this.m_contents[addToIdx].getQuantity();
            final short b = item.getQuantity();
            qty = (short)((a < b) ? a : b);
            oldQty = this.m_contents[addToIdx].getQuantity();
            this.m_contents[addToIdx].updateQuantity(qty);
        }
        if (item.getQuantity() - qty > 0) {
            if (addToIdx != -1) {
                this.m_contents[addToIdx].updateQuantity((short)(-qty));
            }
            if (item.getQuantity() > 0 && newIdx != -1) {
                this.m_contents[newIdx] = item;
                this.m_idxByUniqueId.put(item.getUniqueId(), newIdx);
                this.m_contents[newIdx] = null;
                this.m_idxByUniqueId.remove(item.getUniqueId());
                return true;
            }
        }
        else if (oldQty != -1) {
            this.m_contents[addToIdx].setQuantity(oldQty);
            return true;
        }
        return false;
    }
    
    @Deprecated
    public boolean simulateUpdateQuantity(final C item, final short quantityUpdate) {
        C get = this.getWithUniqueId(item.getUniqueId());
        if (get == null) {
            for (final C type : this.getAllWithReferenceId(item.getReferenceId())) {
                if (type.getQuantity() > 1) {
                    get = type;
                }
            }
        }
        if (get == null) {
            return false;
        }
        if (get.getQuantity() + quantityUpdate <= 0) {
            this.quickRemove(get);
        }
        else {
            get.updateQuantity(quantityUpdate);
        }
        return true;
    }
    
    @Deprecated
    public boolean simulateUpdateQuantity(final long uniqueId, final short quantityUpdate) {
        final C get = this.getWithUniqueId(uniqueId);
        if (get == null) {
            return false;
        }
        if (get.getQuantity() + quantityUpdate <= 0) {
            this.quickRemove(get);
        }
        else {
            get.updateQuantity(quantityUpdate);
        }
        return true;
    }
    
    public InventoryContentProvider<C, R> getContentProvider() {
        return this.m_contentProvider;
    }
    
    private boolean quickRemove(@NotNull final C item) {
        if (this.m_contentChecker != null && this.m_contentChecker.canRemoveItem(this, item) < 0) {
            return false;
        }
        final short idx = this.m_idxByUniqueId.remove(item.getUniqueId());
        if (idx == 0 && (this.m_contents[idx] == null || this.m_contents[idx].getUniqueId() != item.getUniqueId())) {
            return false;
        }
        this.m_contents[idx] = null;
        return true;
    }
    
    public boolean forEach(final TObjectProcedure<C> procedure) {
        for (final C item : this.m_contents) {
            if (item != null && !procedure.execute(item)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder r = new StringBuilder("[");
        r.append(this.getClass().getSimpleName());
        for (int i = 0, size = this.m_contents.length; i < size; ++i) {
            final C content = (C)this.m_contents[i];
            r.append(' ').append(i).append(": ");
            if (content != null) {
                r.append(content);
            }
            else {
                r.append("{}");
            }
        }
        r.append(']');
        return r.toString();
    }
    
    @Override
    public String getLogRepresentation() {
        return "arrayInventory";
    }
    
    static {
        m_logger = Logger.getLogger((Class)ArrayInventory.class);
    }
}
