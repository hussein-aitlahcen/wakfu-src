package com.ankamagames.wakfu.common.game.item;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class BagInventoryContentChecker implements InventoryContentChecker<Item>
{
    private static final Logger m_logger;
    public static final BagInventoryContentChecker INSTANCE;
    
    @Override
    public int canAddItem(final Inventory<Item> inventory, final Item item) {
        if (!item.isActive()) {
            return -3;
        }
        if (!inventory.isFull()) {
            return 0;
        }
        if (inventory.isStacking()) {
            if (item.getStackFreePlace() > 0 && item.isStackable()) {
                final ArrayList<Item> possibleItems = inventory.getAllWithReferenceId(item.getReferenceItem().getId());
                for (int i = possibleItems.size() - 1; i >= 0; --i) {
                    if (possibleItems.get(i).canStackWith(item) && possibleItems.get(i).getQuantity() + item.getQuantity() <= item.getStackMaximumHeight()) {
                        return 1;
                    }
                }
            }
            return -1;
        }
        return -1;
    }
    
    @Override
    public int canAddItem(final Inventory<Item> inventory, final Item item, final short position) {
        if (!item.isActive()) {
            return -3;
        }
        if (!(inventory instanceof ArrayInventory)) {
            BagInventoryContentChecker.m_logger.warn((Object)"Utilisation du BagInventoryChecker sur un inventaire qui ne contient pas un ArrayInventory", (Throwable)new IllegalArgumentException("Utilisation du BagInventoryChecker sur un inventaire qui ne contient pas un ArrayInventory"));
            return this.canAddItem(inventory, item);
        }
        final InventoryContent it = ((ArrayInventory)inventory).getFromPosition(position);
        if (it == null) {
            return 0;
        }
        if (!inventory.isStacking()) {
            return -1;
        }
        if (!it.canStackWith(item)) {
            return -1;
        }
        return 1;
    }
    
    @Override
    public int canReplaceItem(final Inventory inventory, final Item oldItem, final Item newItem) {
        if (!newItem.isActive()) {
            return -3;
        }
        return 0;
    }
    
    @Override
    public int canRemoveItem(final Inventory inventory, final Item item) {
        return 0;
    }
    
    @Override
    public boolean checkCriterion(final Item item, final EffectUser player, final EffectContext context) {
        return true;
    }
    
    @Override
    public boolean checkCriterion(final Inventory<Item> inventory, final EffectUser player, final EffectContext context) {
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BagInventoryContentChecker.class);
        INSTANCE = new BagInventoryContentChecker();
    }
}
