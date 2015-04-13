package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.temporary.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class TemporaryInventoryController
{
    private final TemporaryInventoryModel m_inventory;
    
    public TemporaryInventoryController(final TemporaryInventory inventory) {
        super();
        this.m_inventory = (TemporaryInventoryModel)inventory;
    }
    
    protected TemporaryInventory getInventory() {
        return this.m_inventory;
    }
    
    public final void addItem(final Item itemToAdd) {
        final boolean hasRemainingQuantity = !this.tryToStackItem(itemToAdd);
        if (hasRemainingQuantity) {
            this.doAddItem(itemToAdd);
        }
    }
    
    public final Item removeItem(final long itemId) throws TemporaryInventoryException {
        final Item item = this.m_inventory.getById(itemId);
        if (item == null) {
            throw new TemporaryInventoryException("Impossible de trouver l'item " + itemId);
        }
        return this.m_inventory.remove(itemId);
    }
    
    public final short getQuantity(final long itemId) throws TemporaryInventoryException {
        final Item item = this.m_inventory.getById(itemId);
        if (item == null) {
            throw new TemporaryInventoryException("Impossible de trouver l'item " + itemId);
        }
        return item.getQuantity();
    }
    
    public final void setQuantity(final long itemId, final short quantity) throws TemporaryInventoryException {
        final Item item = this.m_inventory.getById(itemId);
        if (item == null) {
            throw new TemporaryInventoryException("Impossible de trouver l'item " + itemId);
        }
        this.setQuantity(item, quantity);
    }
    
    @Nullable
    public final Item removeQuantity(final long itemId, final short deltaQty) throws TemporaryInventoryException {
        if (deltaQty < 0) {
            throw new TemporaryInventoryException("Impossible de modifier la quantit\u00e9 de l'item " + itemId + ", on utilise une quantit\u00e9 n\u00e9gative");
        }
        final Item item = this.m_inventory.getById(itemId);
        if (item == null) {
            throw new TemporaryInventoryException("Impossible de trouver l'item " + itemId);
        }
        final int quantity = item.getQuantity() - deltaQty;
        return this.setQuantity(item, (short)quantity);
    }
    
    public final void moveToInventory(final long itemId, final long destination, final short position, final short quantity) throws TemporaryInventoryException {
        final AbstractBag targetBag = this.getBag(destination);
        if (targetBag == null) {
            throw new TemporaryInventoryException("Impossible de trouver le sac");
        }
        final Item item = this.m_inventory.getById(itemId);
        if (item == null) {
            throw new TemporaryInventoryException("Impossible de trouver l'item " + itemId);
        }
        final short actualQuantity = (quantity == -1) ? item.getQuantity() : quantity;
        if (actualQuantity < 0 || actualQuantity > item.getQuantity()) {
            throw new TemporaryInventoryException("Quantit\u00e9 invalide : " + actualQuantity);
        }
        try {
            if (targetBag.canAdd(item, position)) {
                if (!targetBag.addAt(item, position)) {
                    throw new TemporaryInventoryException("Probl\u00e8me \u00e0 l'ajout de l'item.");
                }
                this.m_inventory.remove(itemId);
                if (!targetBag.contains(item)) {
                    item.release();
                }
            }
            else {
                final Item itemInInventory = targetBag.getFromPosition(position);
                if (itemInInventory == null || !itemInInventory.canStackWith(item)) {
                    throw new TemporaryInventoryException("La position n'est pas stackable.");
                }
                final short updateQuantity = (short)Math.min(actualQuantity, Math.min(item.getQuantity(), itemInInventory.getStackFreePlace()));
                targetBag.updateQuantity(itemInInventory.getUniqueId(), updateQuantity);
                if (updateQuantity == item.getQuantity()) {
                    this.m_inventory.remove(itemId);
                }
                else {
                    this.m_inventory.setQuantity(itemId, (short)(item.getQuantity() - updateQuantity));
                }
            }
        }
        catch (InventoryCapacityReachedException e) {
            throw new TemporaryInventoryException("La destination est occup\u00e9e. " + e);
        }
        catch (ContentAlreadyPresentException e2) {
            throw new TemporaryInventoryException("Contenu d\u00e9ja pr\u00e9sent. Triche ?" + e2);
        }
        catch (PositionAlreadyUsedException e3) {
            throw new TemporaryInventoryException("La destination est occup\u00e9e." + e3);
        }
    }
    
    public final void moveToEquipment(final long itemId, final short position) throws TemporaryInventoryException {
        final ItemEquipment equipment = this.getEquipment();
        if (equipment == null) {
            throw new TemporaryInventoryException("Impossible de trouver l'equipement");
        }
        final Item item = this.m_inventory.getById(itemId);
        if (item == null) {
            throw new TemporaryInventoryException("Impossible de trouver l'item " + itemId);
        }
        Item itemToAdd;
        if (item.getQuantity() == 1) {
            itemToAdd = item;
        }
        else {
            itemToAdd = Item.newInstance(item.getReferenceItem());
            itemToAdd.setQuantity((short)1);
        }
        final boolean ok = this.checkEquipmentCriterion(itemToAdd) && ((ArrayInventoryWithoutCheck<Item, R>)equipment).canAdd(itemToAdd, position);
        if (!ok) {
            throw new TemporaryInventoryException("L'emplacement de destination est d\u00e9j\u00e0 occup\u00e9 ou invalide.");
        }
        boolean addedOk = false;
        try {
            ((ArrayInventoryWithoutCheck<Item, R>)equipment).addAt(itemToAdd, position);
            addedOk = true;
            if (item.getReferenceItem().getItemType().getLinkedPositions() != null) {
                for (final EquipmentPosition pos : item.getReferenceItem().getItemType().getLinkedPositions()) {
                    final Item tempItem = item.getInactiveCopy();
                    addedOk &= ((ArrayInventoryWithoutCheck<Item, R>)equipment).addAt(tempItem, pos.getId());
                }
            }
        }
        catch (InventoryCapacityReachedException e) {
            throw new TemporaryInventoryException("La destination est occup\u00e9e. " + e);
        }
        catch (ContentAlreadyPresentException e2) {
            throw new TemporaryInventoryException("Contenu d\u00e9ja pr\u00e9sent. Triche ?" + e2);
        }
        catch (PositionAlreadyUsedException e3) {
            throw new TemporaryInventoryException("La destination est occup\u00e9e." + e3);
        }
        finally {
            if (!addedOk && itemToAdd != item) {
                itemToAdd.release();
            }
        }
        if (item == itemToAdd) {
            this.m_inventory.remove(itemId);
        }
        else {
            this.m_inventory.setQuantity(itemId, (short)(item.getQuantity() - 1));
        }
    }
    
    public void destroy(final InventoryContentValidator<Item> validator) {
        for (short i = (short)(this.m_inventory.size() - 1); i >= 0; --i) {
            final Item item = this.m_inventory.getByIdx(i);
            if (validator.isValid(item)) {
                final Item removedItem = this.m_inventory.remove(item.getUniqueId());
                removedItem.release();
            }
        }
    }
    
    public void clear() {
        this.m_inventory.clear();
    }
    
    protected boolean checkEquipmentCriterion(final Item itemToAdd) {
        return true;
    }
    
    @Nullable
    protected ItemEquipment getEquipment() {
        return null;
    }
    
    @Nullable
    protected AbstractBag getBag(final long uniqueId) {
        return null;
    }
    
    private void doAddItem(final Item itemToAdd) {
        if (this.m_inventory.isFull()) {
            this.m_inventory.removeFirst();
        }
        this.m_inventory.add(itemToAdd);
    }
    
    private boolean tryToStackItem(final Item itemToAdd) {
        for (short i = 0, size = this.m_inventory.size(); i < size; ++i) {
            final Item item = this.m_inventory.getByIdx(i);
            if (item.canStackWith(itemToAdd) && item.getStackFreePlace() != 0) {
                final int qty = Math.min(itemToAdd.getQuantity(), item.getStackFreePlace());
                this.m_inventory.setQuantity(item.getUniqueId(), (short)(item.getQuantity() + qty));
                if (itemToAdd.getQuantity() == qty) {
                    itemToAdd.release();
                    return true;
                }
                itemToAdd.updateQuantity((short)(-qty));
            }
        }
        return false;
    }
    
    @Nullable
    private Item setQuantity(final Item item, final short quantity) throws TemporaryInventoryException {
        if (quantity < 0 || quantity > item.getStackMaximumHeight()) {
            throw new TemporaryInventoryException("Impossible de d\u00e9finir la quantit\u00e9 de l'item " + item.getUniqueId() + " \u00e0 " + quantity);
        }
        Item removedItem = null;
        if (quantity == 0) {
            removedItem = this.m_inventory.remove(item.getUniqueId());
        }
        else {
            this.m_inventory.setQuantity(item.getUniqueId(), quantity);
        }
        return removedItem;
    }
    
    @Override
    public String toString() {
        return "TemporaryInventoryController{m_inventory=" + this.m_inventory + '}';
    }
}
