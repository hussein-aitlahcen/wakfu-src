package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;

public class ContiguousArrayInventory<C extends InventoryContent, R> extends ArrayInventory<C, R>
{
    private static final Logger m_logger;
    
    public ContiguousArrayInventory(final InventoryContentProvider<C, R> contentProvider, final InventoryContentChecker<C> contentChecker, final short maximumSize, final boolean stackable) {
        super(contentProvider, contentChecker, maximumSize, stackable);
    }
    
    private void fillHole(final short beginPosition) {
        short hole = 0;
        for (short i = beginPosition, size = (short)this.m_contents.length; i < size; ++i) {
            if (this.m_contents[i] == null) {
                ++hole;
            }
            else if (hole > 0) {
                this.m_contents[i - hole] = this.m_contents[i];
                this.m_idxByUniqueId.remove(this.m_contents[i].getUniqueId());
                this.m_contents[i] = null;
                this.m_idxByUniqueId.put(this.m_contents[i - hole].getUniqueId(), (short)(i - hole));
            }
        }
    }
    
    @Override
    public C removeAt(final short position) {
        final C contentType = super.removeAt(position);
        if (contentType != null) {
            this.fillHole(position);
        }
        return contentType;
    }
    
    @Override
    public boolean remove(final C item) {
        final boolean bok = super.remove(item);
        if (bok) {
            this.fillHole((short)0);
        }
        return bok;
    }
    
    @Override
    public C removeWithUniqueId(final long itemUniqueId) {
        final C contentType = super.removeWithUniqueId(itemUniqueId);
        if (contentType != null) {
            this.fillHole((short)0);
        }
        return contentType;
    }
    
    @Override
    public int destroyWithReferenceId(final int referenceId) {
        final int removed = super.destroyWithReferenceId(referenceId);
        this.fillHole((short)0);
        return removed;
    }
    
    @Override
    public boolean updateQuantity(final long uniqueId, final short quantityUpdate) {
        final boolean result = super.updateQuantity(uniqueId, quantityUpdate);
        this.fillHole((short)0);
        return result;
    }
    
    @Deprecated
    @Override
    public boolean simulateUpdateQuantity(final C item, final short quantityUpdate) {
        final boolean result = super.simulateUpdateQuantity(item, quantityUpdate);
        this.fillHole((short)0);
        return result;
    }
    
    @Deprecated
    @Override
    public boolean simulateUpdateQuantity(final long uniqueId, final short quantityUpdate) {
        final boolean result = super.simulateUpdateQuantity(uniqueId, quantityUpdate);
        this.fillHole((short)0);
        return result;
    }
    
    public boolean insertAt(final C item, short position) throws InventoryCapacityReachedException, ContentAlreadyPresentException, LastPositionAlreadyUsedException, PositionAlreadyUsedException {
        if (item == null) {
            ContiguousArrayInventory.m_logger.info((Object)"Impossible d'ajouter un item null");
            return false;
        }
        if (position < 0 || position >= this.m_maximumSize) {
            ContiguousArrayInventory.m_logger.info((Object)("Impossible d'ajouter un item : position en dehors des limites : " + position));
            return false;
        }
        if (this.m_contentChecker != null && this.m_contentChecker.canAddItem((Inventory<C>)this, item, position) < 0) {
            ContiguousArrayInventory.m_logger.info((Object)"Position refus\u00e9e par le checker");
            return false;
        }
        if (this.isFull()) {
            throw new InventoryCapacityReachedException("Cannot add item : maximum size of inventory is reached (" + this.getMaximumSize() + ")");
        }
        if (this.m_idxByUniqueId.containsKey(item.getUniqueId())) {
            throw new ContentAlreadyPresentException("Item with uniqueID " + item.getUniqueId() + " is already present in the inventory");
        }
        if (this.m_contents[this.m_contents.length - 1] != null) {
            throw new LastPositionAlreadyUsedException("if the inventory is not full, then we should not have an object in the last position");
        }
        if (position >= this.m_idxByUniqueId.size()) {
            position = (short)this.m_idxByUniqueId.size();
        }
        else {
            for (int i = this.m_contents.length - 1; i > position; --i) {
                this.m_contents[i] = this.m_contents[i - 1];
                this.m_contents[i - 1] = null;
                if (this.m_contents[i] != null) {
                    this.m_idxByUniqueId.put(this.m_contents[i].getUniqueId(), (short)i);
                }
            }
        }
        if (this.m_contents[position] != null) {
            throw new PositionAlreadyUsedException("Cannot add item " + item + " at position " + position + " item " + this.m_contents[position] + "already present");
        }
        this.m_contents[position] = item;
        this.m_idxByUniqueId.put(item.getUniqueId(), position);
        this.notifyObservers(InventoryItemModifiedEvent.checkOutAddEvent(this, item, position));
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ContiguousArrayInventory.class);
    }
}
