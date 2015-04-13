package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import org.apache.log4j.*;
import java.lang.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import gnu.trove.*;

public class ArrayInventoryWithoutCheck<C extends InventoryContent, R> extends Inventory<C>
{
    private static final Logger m_logger;
    private InventoryContentChecker<C> m_checker;
    protected Class<C> m_contentClass;
    protected C[] m_contents;
    protected final TLongShortHashMap m_idxByUniqueId;
    protected final InventoryContentProvider<C, R> m_contentProvider;
    protected final boolean m_serializeQuantity;
    
    @Override
    public InventoryContentChecker<C> getContentChecker() {
        return this.m_checker;
    }
    
    @Override
    public void setContentChecker(final InventoryContentChecker<C> checker) {
        this.m_checker = checker;
    }
    
    public ArrayInventoryWithoutCheck(final Class<C> contentClass, final InventoryContentProvider<C, R> contentProvider, final InventoryContentChecker<C> checker, final short maximumSize, final boolean stackable, final boolean serializeQuantity) {
        super(stackable, maximumSize);
        this.m_idxByUniqueId = new TLongShortHashMap();
        this.m_contentClass = contentClass;
        this.m_checker = checker;
        if (!stackable && serializeQuantity) {
            throw new IllegalArgumentException("Impossible de cr\u00e9er un inventaire \u00e0 la fois non stackable (donc sans quantit\u00e9) et pour lequel on veut s\u00e9rialiser une quantit\u00e9.");
        }
        this.setMaximumSize(maximumSize);
        this.m_contentProvider = contentProvider;
        this.m_serializeQuantity = serializeQuantity;
    }
    
    @Override
    public final boolean setMaximumSize(final short maxSize) {
        if (maxSize < this.getMaximumSize()) {
            ArrayInventoryWithoutCheck.m_logger.error((Object)"Can't decrease the size of an ArrayInventory");
            return false;
        }
        if (this.m_contents != null && maxSize == this.getMaximumSize()) {
            return true;
        }
        super.setMaximumSize(maxSize);
        if (this.m_contents == null) {
            this.m_contents = (InventoryContent[])Array.newInstance(this.m_contentClass, maxSize);
        }
        else {
            final C[] tmp = (C[])Array.newInstance(this.m_contentClass, maxSize);
            System.arraycopy(this.m_contents, 0, tmp, 0, this.m_contents.length);
            this.m_contents = tmp;
        }
        this.m_idxByUniqueId.ensureCapacity(maxSize);
        return true;
    }
    
    public boolean canAdd(final C item) {
        return this.m_checker.canAddItem(this, item) >= 0;
    }
    
    public boolean canAdd(final C item, final short position) {
        return this.m_checker.canAddItem(this, item, position) >= 0;
    }
    
    public boolean canRemove(final long itemId) {
        return this.m_checker.canRemoveItem(this, this.getWithUniqueId(itemId)) >= 0;
    }
    
    public boolean canRemove(final C item) {
        return this.m_checker.canRemoveItem(this, item) >= 0;
    }
    
    @Override
    public boolean add(final C item) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        if (item == null) {
            return false;
        }
        if (item.getQuantity() <= 0) {
            ArrayInventoryWithoutCheck.m_logger.error((Object)("On essaye d'ajouter un item avec une quantit\u00e9 de " + item.getQuantity()), (Throwable)new Exception());
            return false;
        }
        if (this.isFull()) {
            throw new InventoryCapacityReachedException("Cannot add item : maximum size of inventory is reached (" + this.getMaximumSize() + ')');
        }
        if (this.m_idxByUniqueId.containsKey(item.getUniqueId())) {
            throw new ContentAlreadyPresentException("Item with uniqueID " + item.getUniqueId() + " is already present in the inventory");
        }
        short idx = -1;
        for (short i = 0, size = (short)this.m_contents.length; i < size; ++i) {
            if (this.m_contents[i] == null) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            throw new InventoryCapacityReachedException("Cannot add item : no left space for it (strange, should have said Inventory is full before. Size : " + this.size() + " MaxSize : " + this.getMaximumSize());
        }
        this.m_contents[idx] = item;
        this.m_idxByUniqueId.put(item.getUniqueId(), idx);
        this.notifyObservers(InventoryItemModifiedEvent.checkOutAddEvent(this, item, idx));
        return true;
    }
    
    @Override
    public boolean updateQuantity(final long uniqueId, final short quantityUpdate) {
        if (!this.m_stacking) {
            ArrayInventoryWithoutCheck.m_logger.error((Object)"Cannot update quantity of a not stackable inventory");
            return false;
        }
        final short idx = this.m_idxByUniqueId.get(uniqueId);
        if (idx != -1) {
            final C item = (C)this.m_contents[idx];
            if (item != null) {
                if (item.getQuantity() + quantityUpdate <= 0) {
                    return this.destroy(item);
                }
                if (item.getQuantity() + quantityUpdate > item.getStackMaximumHeight()) {
                    return false;
                }
                item.updateQuantity(quantityUpdate);
                this.notifyObservers(InventoryItemModifiedEvent.checkOutQuantityEvent(this, item, quantityUpdate));
                return true;
            }
        }
        ArrayInventoryWithoutCheck.m_logger.error((Object)"Item Not found");
        return false;
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
            ArrayInventoryWithoutCheck.m_logger.info((Object)"Impossible d'ajouter un item null");
            return false;
        }
        if (position < 0 || position >= this.m_maximumSize) {
            ArrayInventoryWithoutCheck.m_logger.info((Object)("Impossible d'ajouter un item : position en dehors des limites : " + position));
            return false;
        }
        if (this.m_contents[position] != null) {
            if (item.canStackWith(this.m_contents[position]) && item.getQuantity() + this.m_contents[position].getQuantity() < item.getStackMaximumHeight()) {
                this.updateQuantity(this.m_contents[position].getUniqueId(), item.getQuantity());
                return true;
            }
            throw new PositionAlreadyUsedException("Cannot add item " + item + " at position " + position + " item " + this.m_contents[position] + "already present");
        }
        else {
            if (this.m_idxByUniqueId.containsKey(item.getUniqueId())) {
                throw new ContentAlreadyPresentException("Item with uniqueID " + item.getUniqueId() + " is already present in the inventory");
            }
            this.m_contents[position] = item;
            this.m_idxByUniqueId.put(item.getUniqueId(), position);
            this.notifyObservers(InventoryItemModifiedEvent.checkOutAddEvent(this, item, position));
            return true;
        }
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
        if (this.m_contents[idx] != item) {
            ArrayInventoryWithoutCheck.m_logger.error((Object)("Probl\u00e8me de logique : table d'index et tableau incoh\u00e9rents. Item attendu \u00e0 la position " + idx + " : " + item + " item trouv\u00e9 : " + this.m_contents[idx]));
            return false;
        }
        this.m_contents[idx] = null;
        this.m_idxByUniqueId.remove(item.getUniqueId());
        this.notifyObservers(InventoryItemModifiedEvent.checkOutRemoveEvent(this, item, idx));
        return true;
    }
    
    public void destroyItems(final InventoryContentValidator<C> validator) {
        for (final C content : this.m_contents) {
            if (content != null && (validator == null || validator.isValid(content))) {
                this.destroy(content);
            }
        }
    }
    
    @Override
    public boolean destroy(final C item) {
        final boolean ret = this.remove(item);
        item.release();
        return ret;
    }
    
    public C removeAt(final short position) {
        if (position < 0 || position >= this.m_maximumSize) {
            return null;
        }
        final C item = (C)this.m_contents[position];
        if (item == null) {
            return null;
        }
        this.m_contents[position] = null;
        this.m_idxByUniqueId.remove(item.getUniqueId());
        this.notifyObservers(InventoryItemModifiedEvent.checkOutRemoveEvent(this, item, position));
        return item;
    }
    
    public boolean destroyAt(final short position) {
        final C item = this.removeAt(position);
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
        if (this.m_idxByUniqueId.contains(itemUniqueId)) {
            final short pos = this.m_idxByUniqueId.get(itemUniqueId);
            return this.removeAt(pos);
        }
        return null;
    }
    
    public TShortArrayList listOfDestroyedWithReferenceId(final int referenceId) {
        TShortArrayList list = null;
        for (final C content : this.m_contents) {
            if (content != null && content.getReferenceId() == referenceId) {
                final short pos = this.getPosition(content.getUniqueId());
                if (this.destroyAt(pos)) {
                    if (list == null) {
                        list = new TShortArrayList();
                    }
                    list.add(pos);
                }
            }
        }
        return list;
    }
    
    public TShortArrayList listOfDestroyedWithReferenceId(final int referenceId, int count) {
        TShortArrayList list = null;
        for (final C content : this.m_contents) {
            if (content != null && content.getReferenceId() == referenceId) {
                final short pos = this.getPosition(content.getUniqueId());
                if (this.destroyAt(pos)) {
                    if (list == null) {
                        list = new TShortArrayList();
                    }
                    list.add(pos);
                    if (count == 1) {
                        return list;
                    }
                    --count;
                }
            }
        }
        return list;
    }
    
    @Override
    public int destroyWithReferenceId(final int referenceId) {
        return 0;
    }
    
    @Override
    public int destroyWithReferenceId(final int referenceId, final int count) {
        return 0;
    }
    
    @Override
    public Iterator<C> iterator() {
        return new ArrayIterator<C>((C[])this.m_contents, false);
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder("[ ");
        result.append(this.getClass().getSimpleName()).append(' ');
        if (this.m_contents == null) {
            result.append("contents=null");
        }
        else {
            for (int i = 0, size = this.m_contents.length; i < size; ++i) {
                result.append(i).append(':');
                final C content = (C)this.m_contents[i];
                if (content == null) {
                    result.append("<null>");
                }
                else {
                    result.append(content.getClass().getSimpleName()).append(",ref=").append(content.getReferenceId()).append(",unique=").append(content.getUniqueId());
                }
                result.append(' ');
            }
        }
        result.append(" / idxByUid={ ");
        if (this.m_idxByUniqueId != null && !this.m_idxByUniqueId.isEmpty()) {
            this.m_idxByUniqueId.forEachEntry(new TLongShortProcedure() {
                @Override
                public boolean execute(final long a, final short b) {
                    result.append(a).append("=>").append(b).append(' ');
                    return true;
                }
            });
        }
        result.append("} ]");
        return result.toString();
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
    
    @Override
    public C getFirstWithReferenceId(final int referenceId, final InventoryContentValidator<C> validator) {
        for (final C item : this.m_contents) {
            if (item != null && item.getReferenceId() == referenceId && validator.isValid(item)) {
                return item;
            }
        }
        return null;
    }
    
    @Override
    public ArrayList<C> getAllWithReferenceId(final int referenceId) {
        final ArrayList<C> list = new ArrayList<C>();
        for (final C item : this.m_contents) {
            if (item != null && item.getReferenceId() == referenceId) {
                list.add(item);
            }
        }
        return list;
    }
    
    @Override
    public ArrayList<C> getAllWithReferenceId(final int referenceId, final InventoryContentValidator<C> validator) {
        final ArrayList<C> list = new ArrayList<C>();
        for (final C item : this.m_contents) {
            if (item != null && item.getReferenceId() == referenceId && validator.isValid(item)) {
                list.add(item);
            }
        }
        return list;
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
        for (int i = this.m_contents.length - 1; i >= 0; --i) {
            this.m_contents[i] = null;
        }
        this.m_idxByUniqueId.clear();
        if (itemsCount > 0) {
            this.notifyObservers(InventoryClearedEvent.checkOut(this));
        }
        return itemsCount;
    }
    
    @Override
    public int destroyAll() {
        final int itemsCount = this.size();
        for (int i = this.m_contents.length - 1; i >= 0; --i) {
            if (this.m_contents[i] != null) {
                this.m_contents[i].release();
            }
            this.m_contents[i] = null;
        }
        this.m_idxByUniqueId.clear();
        if (itemsCount > 0) {
            this.notifyObservers(InventoryClearedEvent.checkOut(this));
        }
        return itemsCount;
    }
    
    public boolean forEach(final TObjectProcedure<C> procedure) {
        for (final C item : this.m_contents) {
            if (item != null && !procedure.execute(item)) {
                return false;
            }
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ArrayInventoryWithoutCheck.class);
    }
}
