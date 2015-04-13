package com.ankamagames.wakfu.common.game.item;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import java.util.*;
import gnu.trove.*;

public abstract class AbstractBagContainer
{
    private static final Logger m_logger;
    public static final Comparator<AbstractBag> BAG_SORTER;
    public static final Comparator<AbstractBag> BAG_SORTER_TYPED_FIRST;
    protected final TLongObjectHashMap<AbstractBag> m_bagsById;
    protected final ArrayList<BagContainerListener> m_listeners;
    
    public AbstractBagContainer() {
        super();
        this.m_bagsById = new TLongObjectHashMap<AbstractBag>();
        this.m_listeners = new ArrayList<BagContainerListener>();
    }
    
    public boolean containsBag(final long bagUid) {
        return this.m_bagsById.containsKey(bagUid);
    }
    
    public AbstractBag get(final long id) {
        return this.m_bagsById.get(id);
    }
    
    public AbstractBag getFromUid(final long uid) {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            final AbstractBag bag = it.value();
            if (bag.getUid() == uid) {
                return bag;
            }
        }
        return null;
    }
    
    public TLongObjectIterator<AbstractBag> getBagsIterator() {
        return this.m_bagsById.iterator();
    }
    
    public AbstractBag getFirstContainerWithFreePlaceFor(final Item item) {
        for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER_TYPED_FIRST)) {
            if (bag.m_inventory.getContentChecker().canAddItem((Inventory<Item>)bag.m_inventory, item) >= 0) {
                return bag;
            }
        }
        return null;
    }
    
    public AbstractBag getFirstContainerWith(final long id) {
        for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER)) {
            if (bag.m_inventory.containsUniqueId(id)) {
                return bag;
            }
        }
        return null;
    }
    
    public AbstractBag addItemToBags(final Item itemToAdd) {
        return this.addItemToBags(itemToAdd, null);
    }
    
    public AbstractBag addItemToBags(final Item itemToAdd, final AbstractBagsOperationVisitor visitor) {
        return this.addItemToBags(itemToAdd, false, visitor);
    }
    
    public AbstractBag addItemToBags(final Item itemToAdd, final boolean releaseItemIfStacked) {
        return this.addItemToBags(itemToAdd, releaseItemIfStacked, null);
    }
    
    public AbstractBag addItemToBags(final Item itemToAdd, final boolean releaseItemIfStacked, @Nullable final InventoryObserver visitor) {
        AbstractBag targetBag = null;
        if (itemToAdd.getStackMaximumHeight() > 1) {
            targetBag = this.getFirstBagWithStackablePlace(itemToAdd);
        }
        if (targetBag == null) {
            targetBag = this.getFirstContainerWithFreePlaceFor(itemToAdd);
        }
        if (targetBag == null) {
            return null;
        }
        final ArrayInventory<Item, RawInventoryItem> inv = targetBag.m_inventory;
        try {
            if (visitor != null) {
                inv.addObserver(visitor);
            }
            if (inv.add(itemToAdd)) {
                if (releaseItemIfStacked && !inv.contains(itemToAdd)) {
                    itemToAdd.release();
                }
                return targetBag;
            }
            return null;
        }
        catch (InventoryCapacityReachedException e) {
            AbstractBagContainer.m_logger.error((Object)"Capacit\u00e9 de l'inventaire cible atteinte. Erreur, on a r\u00e9cup\u00e9r\u00e9 ce sac comme disponible pour cet objet !", (Throwable)e);
        }
        catch (ContentAlreadyPresentException e2) {
            AbstractBagContainer.m_logger.error((Object)"L'item \u00e9tait d\u00e9ja pr\u00e9sent dans le sac de destination. ID Dupliqu\u00e9 ?", (Throwable)e2);
        }
        finally {
            if (visitor != null) {
                inv.removeObserver(visitor);
            }
        }
        return null;
    }
    
    public AbstractBag removeItemFromBags(final Item itemToRemove) {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            final AbstractBag container = it.value();
            if (container.m_inventory.remove(itemToRemove)) {
                return container;
            }
        }
        return null;
    }
    
    public void destroyItems(final InventoryContentValidator<Item> validator) {
        this.m_bagsById.forEachValue(new TObjectProcedure<AbstractBag>() {
            @Override
            public boolean execute(final AbstractBag object) {
                object.destroyItems(validator);
                return true;
            }
        });
    }
    
    public int destroyItemFromBags(final int referenceId) {
        int removed = 0;
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            final AbstractBag container = it.value();
            removed += container.m_inventory.destroyWithReferenceId(referenceId);
        }
        return removed;
    }
    
    public int destroyItemFromBags(final int referenceId, final int count) {
        int removed = 0;
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext() && count > 0) {
            it.advance();
            final AbstractBag container = it.value();
            removed += container.m_inventory.destroyWithReferenceId(referenceId, count);
            if (removed >= count) {
                return removed;
            }
        }
        return removed;
    }
    
    public void cleanAllBags() {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().m_inventory.cleanup();
        }
    }
    
    public Item removeItemFromBags(final long itemId) {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            final Item item = it.value().m_inventory.removeWithUniqueId(itemId);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
    
    public short getQuantity(final long itemId) {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        short quantity = 0;
        while (it.hasNext()) {
            it.advance();
            quantity += it.value().m_inventory.getQuantity(itemId);
        }
        return quantity;
    }
    
    public boolean updateQuantity(final long itemUId, final short quantityUpdate) {
        return this.updateQuantity(itemUId, quantityUpdate, null);
    }
    
    public boolean updateQuantity(final long itemUId, final short quantityUpdate, @Nullable final AbstractBagsOperationVisitor visitor) {
        boolean updated = false;
        for (TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator(); it.hasNext() && !updated; updated = it.value().updateQuantity(itemUId, quantityUpdate, visitor)) {
            it.advance();
        }
        return updated;
    }
    
    public InventoryContent getFirstItemFromInventory(final int itemReferenceId) {
        for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER)) {
            final InventoryContent item = bag.m_inventory.getFirstWithReferenceId(itemReferenceId);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
    
    public Item getFirstItemFromInventoryFromTypeChildOf(final AbstractItemType typeId) {
        for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER)) {
            final Item item = bag.getFirstItemWithTypeChildOf(typeId);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
    
    public Item getFirstItemFromInventoryWithProperty(final ItemProperty property) {
        for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER)) {
            final Item item = bag.getFirstItemWithProperty(property);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
    
    public Item getFirstItemFromInventoryFromRefId(final int itemRefId) {
        for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER)) {
            final Item item = bag.getFirstWithReferenceId(itemRefId);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
    
    public Item getFirstItemFromInventoryFromRefId(final int itemRefId, final InventoryContentValidator<Item> validator) {
        for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER)) {
            final Item item = bag.getFirstWithReferenceId(itemRefId, validator);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
    
    public Item getFirstItemFromInventory() {
        for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER)) {
            for (final Item item : bag.m_inventory) {
                if (item != null) {
                    return item;
                }
            }
        }
        return null;
    }
    
    public AbstractBag getFirstInventoryFromItem(final int itemReferenceId) {
        for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER)) {
            final InventoryContent item = bag.m_inventory.getFirstWithReferenceId(itemReferenceId);
            if (item != null) {
                return bag;
            }
        }
        return null;
    }
    
    public Item getItemFromInventories(final long itemId) {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            final Item item = it.value().m_inventory.getWithUniqueId(itemId);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
    
    public ArrayList<Item> getItemListFromInventories(final int refItemId) {
        ArrayList<Item> list = null;
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            final ArrayList<Item> items = it.value().m_inventory.getAllWithReferenceId(refItemId);
            if (list == null) {
                list = items;
            }
            else {
                for (int i = 0, size = items.size(); i < size; ++i) {
                    list.add(items.get(i));
                }
            }
        }
        return list;
    }
    
    public AbstractBag getFirstBagWithStackablePlace(final Item item) {
        for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER_TYPED_FIRST)) {
            final Iterable<Item> refItemPositions = bag.m_inventory.getAllWithReferenceId(item.getReferenceId());
            for (final InventoryContent possiblePositions : refItemPositions) {
                if (possiblePositions.canStackWith(item) && possiblePositions.getQuantity() + item.getQuantity() <= item.getStackMaximumHeight()) {
                    return bag;
                }
            }
        }
        return null;
    }
    
    protected final ArrayList<AbstractBag> getOrderedBags(final Comparator<AbstractBag> sorter) {
        final ArrayList<AbstractBag> orderedBags = new ArrayList<AbstractBag>();
        this.m_bagsById.forEachValue(new TObjectProcedure<AbstractBag>() {
            @Override
            public boolean execute(final AbstractBag bag) {
                orderedBags.add(bag);
                return true;
            }
        });
        Collections.sort(orderedBags, sorter);
        return orderedBags;
    }
    
    public boolean canAddItemToBags(final Item item) {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            final InventoryContentChecker<Item> contentChecker = it.value().m_inventory.getContentChecker();
            if (contentChecker != null && contentChecker.canAddItem((Inventory<Item>)it.value().m_inventory, item) >= 0) {
                return true;
            }
        }
        return false;
    }
    
    public AbstractBag contains(final InventoryContent item) {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value().m_inventory.containsUniqueId(item.getUniqueId())) {
                return it.value();
            }
        }
        return null;
    }
    
    public boolean contains(final long itemUID) {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value().m_inventory.containsUniqueId(itemUID)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean addListener(final BagContainerListener l) {
        return !this.m_listeners.contains(l) && this.m_listeners.add(l);
    }
    
    public boolean removeListener(final BagContainerListener l) {
        return this.m_listeners.remove(l);
    }
    
    public void addContainer(final AbstractBag bag) {
        this.m_bagsById.put(bag.getUid(), bag);
        this.bagAdded(bag);
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).bagAdded(bag);
        }
    }
    
    public AbstractBag removeContainerById(final long id) {
        final AbstractBag bag = this.m_bagsById.remove(id);
        this.bagRemoved(bag);
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).bagRemoved(bag);
        }
        return bag;
    }
    
    protected abstract void bagAdded(final AbstractBag p0);
    
    protected abstract void bagRemoved(final AbstractBag p0);
    
    public void clean() {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            for (int i = 0; i < this.m_listeners.size(); ++i) {
                this.m_listeners.get(i).bagRemoved(it.value());
            }
        }
        this.m_bagsById.clear();
        this.m_listeners.clear();
    }
    
    public TLongIntHashMap removeQuantity(final int refId, final int quantity, @Nullable final InventoryObserver visitor) {
        final TLongIntHashMap result = new TLongIntHashMap();
        int findQuantity = 0;
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            final AbstractBag container = it.value();
            if (visitor != null) {
                container.m_inventory.addObserver(visitor);
            }
            try {
                final ArrayList<Item> stacks = container.getAllWithReferenceId(refId);
                if (stacks.isEmpty()) {
                    continue;
                }
                for (int i = stacks.size() - 1; i >= 0; --i) {
                    final Item content = stacks.get(i);
                    if (!content.isRent()) {
                        final short quantityToRemove = (short)Math.min(quantity - findQuantity, content.getQuantity());
                        result.put(content.getUniqueId(), -quantityToRemove);
                        findQuantity += quantityToRemove;
                        container.m_inventory.updateQuantity(content.getUniqueId(), (short)(-quantityToRemove));
                        if (findQuantity == quantity) {
                            return result;
                        }
                    }
                }
            }
            finally {
                if (visitor != null) {
                    container.m_inventory.removeObserver(visitor);
                }
            }
        }
        return result;
    }
    
    public int getQuantityForRefId(final int refId) {
        return this.getQuantityForRefId(refId, false);
    }
    
    public int getQuantityForRefId(final int refId, final boolean withRent) {
        int findQuantity = 0;
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            final AbstractBag container = it.value();
            final ArrayList<Item> stacks = container.m_inventory.getAllWithReferenceId(refId);
            if (stacks.isEmpty()) {
                continue;
            }
            for (int i = stacks.size() - 1; i >= 0; --i) {
                final Item content = stacks.get(i);
                if (withRent || !content.isRent()) {
                    findQuantity += content.getQuantity();
                }
            }
        }
        return findQuantity;
    }
    
    public int getNbFreePlacesFor(final TLongObjectIterator<Item> items) {
        int freePlaces = 0;
        final TByteIntHashMap nbFreeByBag = new TByteIntHashMap();
        while (items.hasNext()) {
            items.advance();
            final Item item = items.value();
            for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER_TYPED_FIRST)) {
                final InventoryContentChecker<Item> contentChecker = bag.getContentChecker();
                if (contentChecker != null && contentChecker.canAddItem((Inventory<Item>)bag.m_inventory, item) != 0) {
                    continue;
                }
                final byte bagPosition = bag.getPosition();
                if (nbFreeByBag.containsKey(bagPosition)) {
                    final int currentFreePlaces = nbFreeByBag.get(bagPosition);
                    if (currentFreePlaces == 0) {
                        continue;
                    }
                    nbFreeByBag.put(bagPosition, currentFreePlaces - 1);
                }
                else {
                    nbFreeByBag.put(bagPosition, bag.getNbFreePlaces() - 1);
                }
                ++freePlaces;
            }
        }
        return freePlaces;
    }
    
    public int getNbFreePlaces() {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        int freePlaces = 0;
        while (it.hasNext()) {
            it.advance();
            final AbstractBag bag = it.value();
            if (bag.getContentChecker() instanceof TypedBagInventoryContentChecker) {
                continue;
            }
            freePlaces += bag.getNbFreePlaces();
        }
        return freePlaces;
    }
    
    public short getPossibleDestination(final Item item) {
        short possibleDestinations = 0;
        for (final AbstractBag bag : this.getOrderedBags(AbstractBagContainer.BAG_SORTER_TYPED_FIRST)) {
            try {
                if (!bag.simulateAddWithRemove(item)) {
                    continue;
                }
                ++possibleDestinations;
            }
            catch (InventoryCapacityReachedException e) {
                AbstractBagContainer.m_logger.warn((Object)"L'inventaire de destination est plein, erreur de simulateAdd", (Throwable)e);
            }
            catch (ContentAlreadyPresentException e2) {
                AbstractBagContainer.m_logger.error((Object)"l'item test\u00e9 est d\u00e9ja pr\u00e9sent dans l'inventaire cible. Erreur, ID Dupliqu\u00e9 ? ", (Throwable)e2);
            }
        }
        return possibleDestinations;
    }
    
    public ArrayList<Item> getAllWithReferenceId(final int referenceId) {
        final ArrayList<Item> items = new ArrayList<Item>();
        final TLongObjectIterator<AbstractBag> it = this.getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            items.addAll(it.value().getAllWithReferenceId(referenceId));
        }
        return items;
    }
    
    public ArrayList<Item> getAllWithReferenceId(final int referenceId, final InventoryContentValidator validator) {
        final ArrayList<Item> items = new ArrayList<Item>();
        final TLongObjectIterator<AbstractBag> it = this.getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            items.addAll(it.value().getAllWithReferenceId(referenceId, validator));
        }
        return items;
    }
    
    public ArrayList<Item> getAllWithValidator(final InventoryContentValidator validator) {
        final ArrayList<Item> items = new ArrayList<Item>();
        final TLongObjectIterator<AbstractBag> it = this.getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            items.addAll(it.value().getAllWithValidator(validator));
        }
        return items;
    }
    
    public AbstractBag getBagFromPosition(final byte position) {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            final AbstractBag bag = it.value();
            if (bag.getPosition() == position) {
                return bag;
            }
        }
        return null;
    }
    
    public AbstractBag getBagFromUid(final long uid) {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            final AbstractBag bag = it.value();
            if (bag.getUid() == uid) {
                return bag;
            }
        }
        return null;
    }
    
    public boolean forEachItem(final TObjectProcedure<Item> procedure) {
        final TLongObjectIterator<AbstractBag> it = this.m_bagsById.iterator();
        while (it.hasNext()) {
            it.advance();
            if (!it.value().forEachItem(procedure)) {
                return false;
            }
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractBagContainer.class);
        BAG_SORTER = new Comparator<AbstractBag>() {
            @Override
            public int compare(final AbstractBag o1, final AbstractBag o2) {
                return o1.getPosition() - o2.getPosition();
            }
        };
        BAG_SORTER_TYPED_FIRST = new Comparator<AbstractBag>() {
            @Override
            public int compare(final AbstractBag o1, final AbstractBag o2) {
                final boolean bag1IsTyped = o1.isTyped();
                final boolean bag2IsTyped = o2.isTyped();
                if (bag1IsTyped && !bag2IsTyped) {
                    return -1;
                }
                if (!bag1IsTyped && bag2IsTyped) {
                    return 1;
                }
                return AbstractBagContainer.BAG_SORTER.compare(o1, o2);
            }
        };
    }
}
