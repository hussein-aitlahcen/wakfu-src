package com.ankamagames.wakfu.common.game.inventory;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class EquipmentToInventoryExchanger extends AbstractInventoryContentExchanger<ItemEquipment, ArrayInventory<Item, RawInventoryItem>>
{
    protected static final EquipmentToInventoryExchanger m_instance;
    
    @Override
    public int moveItem(final ItemEquipment sourceInventory, final short sourcePosition, final ArrayInventory<Item, RawInventoryItem> targetInventory, final short targetPosition, final Item item, final EffectUser player, final EffectContext context) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        return -1;
    }
    
    @Override
    public int moveItem(final ItemEquipment sourceInventory, final ArrayInventory<Item, RawInventoryItem> targetInventory, final Item item, final EffectUser player, final EffectContext context) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        final int removeResult = ((ArrayInventoryWithoutCheck<Item, R>)sourceInventory).getContentChecker().canRemoveItem((Inventory<Item>)sourceInventory, item);
        final int addResult = targetInventory.getContentChecker().canAddItem((Inventory<Item>)targetInventory, item);
        final short sourcePosition = ((ArrayInventoryWithoutCheck<Item, R>)sourceInventory).getPosition(item);
        if (removeResult < 0 || addResult < 0) {
            return 1;
        }
        if (this.removeFromEquipment(item, sourceInventory)) {}
        if (!((ArrayInventoryWithoutCheck<Item, R>)sourceInventory).getContentChecker().checkCriterion((Inventory<Item>)sourceInventory, player, context)) {
            try {
                this.addItemToEquipment(item, sourceInventory, sourcePosition);
            }
            catch (PositionAlreadyUsedException e) {
                return 1;
            }
            return 1;
        }
        if (!targetInventory.add(item)) {
            return 1;
        }
        return 0;
    }
    
    public int moveItem(final ItemEquipment sourceInventory, final ArrayInventory<Item, RawInventoryItem> targetInventory, final short targetPosition, final Item item, final EffectUser player, final EffectContext context) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        final Item targetItem = targetInventory.getFromPosition(targetPosition);
        if (targetPosition >= 0 && targetItem != null && !item.canStackWith(targetItem)) {
            return 1;
        }
        final int removeResult = ((ArrayInventoryWithoutCheck<Item, R>)sourceInventory).getContentChecker().canRemoveItem((Inventory<Item>)sourceInventory, item);
        final int addResult = targetInventory.getContentChecker().canAddItem((Inventory<Item>)targetInventory, item);
        final short sourcePosition = ((ArrayInventoryWithoutCheck<Item, R>)sourceInventory).getPosition(item);
        if (removeResult < 0 || addResult < 0) {
            return 1;
        }
        if (!this.removeFromEquipment(item, sourceInventory)) {
            return 1;
        }
        boolean b_addOk = false;
        try {
            if (targetPosition >= 0) {
                if (targetItem != null) {
                    b_addOk = targetInventory.updateQuantity(targetItem.getUniqueId(), item.getQuantity());
                }
                else {
                    b_addOk = targetInventory.addAt(item, targetPosition);
                }
            }
            else {
                b_addOk = targetInventory.add(item);
            }
        }
        catch (Exception e) {
            EquipmentToInventoryExchanger.m_logger.trace((Object)"Impossible d'ajouter l'objet a la position donn\u00e9", (Throwable)e);
        }
        if (b_addOk) {
            return 0;
        }
        try {
            this.addItemToEquipment(item, sourceInventory, sourcePosition);
        }
        catch (PositionAlreadyUsedException e2) {
            EquipmentToInventoryExchanger.m_logger.error((Object)"Impossible de remettre l'objet a sa position d'origine", (Throwable)e2);
        }
        return 1;
    }
    
    public static EquipmentToInventoryExchanger getInstance() {
        return EquipmentToInventoryExchanger.m_instance;
    }
    
    static {
        m_instance = new EquipmentToInventoryExchanger();
    }
}
