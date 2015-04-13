package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class CosmeticsInventoryController
{
    private final CosmeticsInventoryModel m_inventory;
    
    public CosmeticsInventoryController(final CosmeticsInventory inventory) {
        super();
        this.m_inventory = (CosmeticsInventoryModel)inventory;
    }
    
    protected CosmeticsInventoryModel getInventory() {
        return this.m_inventory;
    }
    
    public final void addItem(final int itemId) throws CosmeticsInventoryException {
        final CosmeticsItem item = this.m_inventory.getItem(itemId);
        if (item != null) {
            throw new CosmeticsInventoryException("Un item " + item + " est d\u00e9j\u00e0 pr\u00e9sent dans l'inventaire");
        }
        final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(itemId);
        if (refItem == null) {
            throw new CosmeticsInventoryException("Impossible de trouver le refItem " + itemId);
        }
        final CosmeticsItem newItem = new CosmeticsItemModel(refItem.getId());
        this.m_inventory.add(newItem);
    }
    
    public final void addItem(final CosmeticsItem item) throws CosmeticsInventoryException {
        if (!(item instanceof CosmeticsItemModel)) {
            throw new CosmeticsInventoryException("Impossible d'ajouter un item de type " + item.getClass().getSimpleName());
        }
        if (this.m_inventory.getItem(item.getRefId()) != null) {
            throw new CosmeticsInventoryException("Un item " + item + " est d\u00e9j\u00e0 pr\u00e9sent dans l'inventaire");
        }
        this.m_inventory.add(item);
    }
    
    public final CosmeticsItem removeItem(final int refId) throws CosmeticsInventoryException {
        final CosmeticsItem item = this.m_inventory.remove(refId);
        if (item == null) {
            throw new CosmeticsInventoryException("L'item \u00e0 retirer " + refId + " n'\u00e9tait pas dans l'inventaire");
        }
        return item;
    }
    
    @Override
    public String toString() {
        return "CosmeticsInventoryController{m_inventory=" + this.m_inventory + '}';
    }
}
