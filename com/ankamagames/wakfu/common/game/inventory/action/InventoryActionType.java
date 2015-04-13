package com.ankamagames.wakfu.common.game.inventory.action;

import com.ankamagames.framework.kernel.core.common.*;

public enum InventoryActionType
{
    ADD_ITEM(InventoryAddItemAction.FACTORY), 
    REMOVE_ITEM(InventoryRemoveItemAction.FACTORY), 
    MOVE_ITEM(InventoryMoveItemAction.FACTORY), 
    ADD_MONEY(InventoryAddMoneyAction.FACTORY), 
    REMOVE_MONEY(InventoryRemoveMoneyAction.FACTORY), 
    GAME_ADD_ITEM(InventoryAddItemAction.FACTORY);
    
    private final SimpleObjectFactory<InventoryAction> m_factory;
    
    private InventoryActionType(final SimpleObjectFactory<InventoryAction> factory) {
        this.m_factory = factory;
    }
    
    public static InventoryAction createNew(final byte ordinal) throws UnsupportedOperationException, ArrayIndexOutOfBoundsException {
        final InventoryActionType type = values()[ordinal];
        if (type == null) {
            throw new UnsupportedOperationException("Impossible de trouver le type d'action " + type);
        }
        return type.m_factory.createNew();
    }
}
