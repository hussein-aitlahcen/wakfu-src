package com.ankamagames.wakfu.common.game.inventory;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class InventoryToInventoryExchanger extends AbstractInventoryContentExchanger<ArrayInventory<Item, RawInventoryItem>, ArrayInventory<Item, RawInventoryItem>>
{
    protected static final InventoryToInventoryExchanger m_instance;
    
    @Override
    public int moveItem(final ArrayInventory<Item, RawInventoryItem> sourceInventory, final ArrayInventory<Item, RawInventoryItem> targetInventory, final Item item, final EffectUser player, final EffectContext context) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        return -1;
    }
    
    public int moveItem(final ArrayInventory<Item, RawInventoryItem> sourceInventory, final ArrayInventory<Item, RawInventoryItem> targetInventory, final short targetPosition, final Item item, final short movedQuantity, final Item splittedItem, final EffectUser player, final EffectContext context) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        if (item.getQuantity() > movedQuantity && movedQuantity != -1) {
            final Item itemToReplace = targetInventory.getFromPosition(targetPosition);
            if (itemToReplace == item) {
                InventoryToInventoryExchanger.m_logger.warn((Object)"on veut d\u00e9placer un item o\u00f9 il est d\u00e9ja ! ");
                return 1;
            }
            if (itemToReplace != null) {
                if (!itemToReplace.canStackWith(item)) {
                    return 1;
                }
                if (itemToReplace.getQuantity() + movedQuantity < itemToReplace.getStackMaximumHeight()) {
                    itemToReplace.updateQuantity(movedQuantity);
                    item.updateQuantity((short)(-movedQuantity));
                    return 0;
                }
                final int quantity = itemToReplace.getStackMaximumHeight() - itemToReplace.getQuantity();
                itemToReplace.updateQuantity((short)quantity);
                if (item.getQuantity() <= quantity) {
                    sourceInventory.destroy(item);
                }
                else {
                    item.updateQuantity((short)(-quantity));
                }
                return 0;
            }
            else {
                splittedItem.setQuantity(movedQuantity);
                if (targetInventory.getContentChecker().canAddItem((Inventory<Item>)targetInventory, splittedItem) >= 0) {
                    sourceInventory.updateQuantity(item.getUniqueId(), (short)(-movedQuantity));
                    try {
                        targetInventory.addAt(splittedItem, targetPosition);
                    }
                    catch (Exception e) {
                        InventoryToInventoryExchanger.m_logger.error((Object)"Impossible d'ajouter l'objet a la position donn\u00e9", (Throwable)e);
                    }
                    return 0;
                }
                return 1;
            }
        }
        else {
            final Item itemToReplace = targetInventory.getFromPosition(targetPosition);
            if (itemToReplace == item) {
                InventoryToInventoryExchanger.m_logger.warn((Object)"on veut d\u00e9placer un item o\u00f9 il est d\u00e9ja ! ");
                return 1;
            }
            if (itemToReplace == null) {
                if (sourceInventory.getContentChecker().canRemoveItem((Inventory<Item>)sourceInventory, item) >= 0 && targetInventory.getContentChecker().canAddItem((Inventory<Item>)targetInventory, item) >= 0 && sourceInventory.remove(item)) {
                    try {
                        targetInventory.addAt(item, targetPosition);
                    }
                    catch (Exception e) {
                        InventoryToInventoryExchanger.m_logger.error((Object)"Impossible d'ajouter l'objet a la position donn\u00e9", (Throwable)e);
                    }
                    return 0;
                }
                return 1;
            }
            else if (!itemToReplace.canStackWith(item)) {
                if (targetInventory.getContentChecker().canReplaceItem((Inventory<Item>)targetInventory, itemToReplace, item) < 0 || sourceInventory.getContentChecker().canReplaceItem((Inventory<Item>)sourceInventory, item, itemToReplace) < 0) {
                    return 1;
                }
                final short initialPosition = sourceInventory.getPosition(item.getUniqueId());
                sourceInventory.remove(item);
                targetInventory.remove(itemToReplace);
                try {
                    sourceInventory.addAt(itemToReplace, initialPosition);
                }
                catch (Exception e2) {
                    InventoryToInventoryExchanger.m_logger.error((Object)("Erreur lors de l ajout de l'objet a la position" + initialPosition + " dans "), (Throwable)e2);
                }
                try {
                    targetInventory.addAt(item, targetPosition);
                }
                catch (Exception e2) {
                    InventoryToInventoryExchanger.m_logger.error((Object)("Erreur lors de l ajout de l'objet a la position" + targetPosition + " dans "), (Throwable)e2);
                }
                if (sourceInventory.getContentChecker().checkCriterion((Inventory<Item>)sourceInventory, player, context)) {
                    return 0;
                }
                sourceInventory.remove(itemToReplace);
                targetInventory.remove(item);
                try {
                    sourceInventory.addAt(item, initialPosition);
                }
                catch (Exception e2) {
                    InventoryToInventoryExchanger.m_logger.error((Object)("Erreur lors de l ajout de l'objet a la position" + initialPosition + " dans "), (Throwable)e2);
                }
                try {
                    targetInventory.addAt(itemToReplace, targetPosition);
                }
                catch (Exception e2) {
                    InventoryToInventoryExchanger.m_logger.error((Object)("Erreur lors de l ajout de l'objet a la position" + targetPosition + " dans "), (Throwable)e2);
                }
                return 1;
            }
            else {
                if (itemToReplace.getQuantity() + item.getQuantity() <= itemToReplace.getStackMaximumHeight()) {
                    targetInventory.updateQuantity(itemToReplace.getUniqueId(), item.getQuantity());
                    sourceInventory.destroy(item);
                    return 0;
                }
                final int quantity = itemToReplace.getStackMaximumHeight() - itemToReplace.getQuantity();
                targetInventory.updateQuantity(itemToReplace.getUniqueId(), (short)quantity);
                sourceInventory.updateQuantity(item.getUniqueId(), (short)(-quantity));
                return 0;
            }
        }
    }
    
    @Override
    public int moveItem(final ArrayInventory<Item, RawInventoryItem> sourceInventory, final short sourcePosition, final ArrayInventory<Item, RawInventoryItem> targetInventory, final short targetPosition, final Item item, final EffectUser player, final EffectContext context) throws InventoryCapacityReachedException, ContentAlreadyPresentException {
        return -1;
    }
    
    public static InventoryToInventoryExchanger getInstance() {
        return InventoryToInventoryExchanger.m_instance;
    }
    
    static {
        m_instance = new InventoryToInventoryExchanger();
    }
}
