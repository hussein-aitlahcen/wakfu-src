package com.ankamagames.wakfu.common.game.collector.free;

import com.ankamagames.wakfu.common.game.collector.limited.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class CollectorInventoryCheckerFree extends CollectorInventoryChecker implements InventoryContentChecker<Item>
{
    public CollectorInventoryCheckerFree(final IECollectorParameter param) {
        super(param);
    }
    
    @Override
    public boolean canAddCash(final Wallet wallet, final int amountOfCash) {
        return true;
    }
    
    @Override
    public boolean canSubCash(final Wallet wallet, final int amountOfCash) {
        return !this.m_param.isLocked() && wallet.getAmountOfCash() >= amountOfCash;
    }
    
    public int canRemoveItem(final Inventory<Item> inventory, final long itemUid, final short qty) {
        final Item item = inventory.getWithUniqueId(itemUid);
        if (item == null) {
            return -2;
        }
        final short itemQty = item.getQuantity();
        final int realQty = Math.min(qty, itemQty);
        final int canRemove = (realQty == itemQty) ? this.canRemoveItem((Inventory)inventory, item) : ((realQty < itemQty) ? 0 : -1);
        if (canRemove < 0) {
            return canRemove;
        }
        return 0;
    }
    
    @Override
    public int canAddItem(final Inventory inventory, final Item item) {
        final ArrayList<Item> items = inventory.getAllWithReferenceId(item.getReferenceId());
        short qty = item.getQuantity();
        if (item.isRent()) {
            return -3;
        }
        final SimpleCriterion criterion = item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE);
        if (item.isBound() || (criterion != null && !criterion.isValid(null, null, null, null))) {
            return -4;
        }
        Item stack;
        for (int i = 0; i < items.size() && qty > 0; qty -= (short)(stack.canStackWith(item) ? stack.getStackFreePlace() : 0), ++i) {
            stack = items.get(i);
        }
        return (qty <= 0 || !inventory.isFull()) ? 0 : -1;
    }
    
    @Override
    public int canAddItem(final Inventory inventory, final Item item, final short position) {
        return -1;
    }
    
    @Override
    public int canReplaceItem(final Inventory inventory, final Item oldItem, final Item newItem) {
        throw new UnsupportedOperationException("Pas de remplacement");
    }
    
    @Override
    public int canRemoveItem(final Inventory inventory, final Item item) {
        return (!this.m_param.isLocked() && inventory.contains(item)) ? 0 : -1;
    }
    
    @Override
    public boolean checkCriterion(final Item item, final EffectUser player, final EffectContext context) {
        throw new UnsupportedOperationException("Pas de v\u00e9rification de crit\u00e8re");
    }
    
    @Override
    public boolean checkCriterion(final Inventory<Item> itemInventory, final EffectUser player, final EffectContext context) {
        throw new UnsupportedOperationException("Pas de v\u00e9rification de crit\u00e8re");
    }
}
