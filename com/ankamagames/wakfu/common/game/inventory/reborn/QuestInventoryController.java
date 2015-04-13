package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class QuestInventoryController
{
    private final QuestInventoryModel m_inventory;
    
    public QuestInventoryController(final QuestInventory inventory) {
        super();
        this.m_inventory = (QuestInventoryModel)inventory;
    }
    
    protected QuestInventoryModel getInventory() {
        return this.m_inventory;
    }
    
    public final void addOrAdjustItem(final int itemId, final short deltaQty) throws QuestInventoryException {
        if (deltaQty <= 0) {
            throw new QuestInventoryException("Impossible d'ajouter une quantit\u00e9 n\u00e9gative " + deltaQty + " \u00e0 l'item " + itemId);
        }
        if (this.m_inventory.getItem(itemId) == null) {
            this.addItem(itemId, deltaQty);
        }
        else {
            this.adjustItemQuantity(itemId, deltaQty);
        }
    }
    
    public final boolean removeOrAdjustItem(final int itemId, final short deltaQty) throws QuestInventoryException {
        if (deltaQty >= 0) {
            throw new QuestInventoryException("Impossible de soustraire une quantit\u00e9 positive " + deltaQty + " \u00e0 l'item " + itemId);
        }
        final QuestItem item = this.m_inventory.getItem(itemId);
        if (item == null) {
            return false;
        }
        if (item.getQuantity() + deltaQty > 0) {
            this.adjustItemQuantity(itemId, deltaQty);
        }
        else {
            this.removeItem(itemId);
        }
        return true;
    }
    
    public final void addItem(final int itemId, final short quantity) throws QuestInventoryException {
        final QuestItem item = this.m_inventory.getItem(itemId);
        if (item != null) {
            throw new QuestInventoryException("Un item " + item + " est d\u00e9j\u00e0 pr\u00e9sent dans l'inventaire");
        }
        final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(itemId);
        if (refItem == null) {
            throw new QuestInventoryException("Impossible de trouver le refItem " + itemId);
        }
        if (quantity <= 0) {
            throw new QuestInventoryException("Impossible de mettre \u00e0 jour la quantit\u00e9 de l'item " + itemId + " d'une quantit\u00e9 de " + quantity);
        }
        final QuestItemModel newItem = new QuestItemModel(refItem);
        newItem.setQuantity(MathHelper.minShort(quantity, refItem.getStackMaximumHeight()));
        this.m_inventory.add(newItem);
    }
    
    public final void addItem(final QuestItem item) throws QuestInventoryException {
        if (!(item instanceof QuestItemModel)) {
            throw new QuestInventoryException("Impossible d'ajouter un item de type " + item.getClass().getSimpleName());
        }
        if (this.m_inventory.getItem(item.getRefId()) != null) {
            throw new QuestInventoryException("Un item " + item + " est d\u00e9j\u00e0 pr\u00e9sent dans l'inventaire");
        }
        this.m_inventory.add(item);
    }
    
    public final short removeItem(final int itemId) throws QuestInventoryException {
        final QuestItem item = this.m_inventory.getItem(itemId);
        if (item == null) {
            throw new QuestInventoryException("L'item " + itemId + " n'existe pas dans l'inventaire");
        }
        this.m_inventory.remove(item);
        return item.getQuantity();
    }
    
    public final void adjustItemQuantity(final int itemId, final short deltaQty) throws QuestInventoryException {
        if (deltaQty == 0) {
            throw new QuestInventoryException("Impossible de mettre \u00e0 jour la quantit\u00e9 de l'item " + itemId + " d'une quantit\u00e9 de " + deltaQty);
        }
        final QuestItemModel item = (QuestItemModel)this.m_inventory.getItem(itemId);
        if (item == null) {
            throw new QuestInventoryException("Impossible de trouver l'item " + itemId);
        }
        final int newQty = item.getQuantity() + deltaQty;
        if (newQty <= 0) {
            throw new QuestInventoryException("Impossible de mettre \u00e0 jour la quantit\u00e9 de l'item " + itemId + " \u00e0 une quantit\u00e9 de " + newQty);
        }
        item.setQuantity(MathHelper.minShort(MathHelper.ensureShort(newQty), item.getStackMaximumHeight()));
    }
    
    public final void setItemQuantity(final int itemId, final short quantity) throws QuestInventoryException {
        final QuestItemModel item = (QuestItemModel)this.m_inventory.getItem(itemId);
        if (item == null) {
            throw new QuestInventoryException("Impossible de trouver l'item " + itemId);
        }
        if (quantity <= 0) {
            throw new QuestInventoryException("Impossible de mettre \u00e0 jour la quantit\u00e9 de l'item " + itemId + " d'une quantit\u00e9 de " + quantity);
        }
        item.setQuantity(MathHelper.minShort(quantity, item.getStackMaximumHeight()));
    }
    
    @Override
    public String toString() {
        return "QuestInventoryController{m_inventory=" + this.m_inventory + '}';
    }
}
