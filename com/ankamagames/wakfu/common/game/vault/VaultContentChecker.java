package com.ankamagames.wakfu.common.game.vault;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class VaultContentChecker implements InventoryContentChecker<Item>
{
    public static final VaultContentChecker INSTANCE;
    
    @Override
    public int canAddItem(final Inventory<Item> inventory, final Item item) {
        final SimpleCriterion exchangeCriterion = item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE);
        if (exchangeCriterion != null && !exchangeCriterion.isValid(null, null, item, null)) {
            return -8;
        }
        final SimpleCriterion dropCriterion = item.getReferenceItem().getCriterion(ActionsOnItem.DROP);
        if (dropCriterion != null && !dropCriterion.isValid(null, null, item, null)) {
            return -8;
        }
        final ArrayList<Item> items = inventory.getAllWithReferenceId(item.getReferenceId());
        short qty = item.getQuantity();
        Item stack;
        for (int i = 0; i < items.size() && qty > 0; qty -= (short)(stack.canStackWith(item) ? stack.getStackFreePlace() : 0), ++i) {
            stack = items.get(i);
        }
        return (qty <= 0 || !inventory.isFull()) ? 0 : -1;
    }
    
    @Override
    public int canAddItem(final Inventory<Item> inventory, final Item item, final short position) {
        final SimpleCriterion exchangeCriterion = item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE);
        if (exchangeCriterion != null && !exchangeCriterion.isValid(null, null, item, null)) {
            return -8;
        }
        final SimpleCriterion dropCriterion = item.getReferenceItem().getCriterion(ActionsOnItem.DROP);
        if (dropCriterion != null && !dropCriterion.isValid(null, null, item, null)) {
            return -8;
        }
        if (position < 0) {
            return -5;
        }
        final ArrayInventory<Item, RawInventoryItem> inv = (ArrayInventory<Item, RawInventoryItem>)(ArrayInventory)inventory;
        final Item targetItem = inv.getFromPosition(position);
        if (targetItem == null) {
            return 0;
        }
        if (!item.canStackWith(targetItem)) {
            return -1;
        }
        return (item.getQuantity() <= targetItem.getStackFreePlace()) ? 1 : -1;
    }
    
    @Override
    public int canReplaceItem(final Inventory<Item> inventory, final Item oldItem, final Item newItem) {
        throw new UnsupportedOperationException("Pas de remplacement");
    }
    
    @Override
    public int canRemoveItem(final Inventory<Item> inventory, final Item item) {
        return inventory.contains(item) ? 0 : -1;
    }
    
    @Override
    public boolean checkCriterion(final Item item, final EffectUser player, final EffectContext context) {
        throw new UnsupportedOperationException("Pas de v\u00e9rification de crit\u00e8re");
    }
    
    @Override
    public boolean checkCriterion(final Inventory<Item> inventory, final EffectUser player, final EffectContext context) {
        throw new UnsupportedOperationException("Pas de v\u00e9rification de crit\u00e8re");
    }
    
    static {
        INSTANCE = new VaultContentChecker();
    }
}
