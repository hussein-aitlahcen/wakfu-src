package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class MerchantInventoryChecker implements InventoryContentChecker<AbstractMerchantInventoryItem>
{
    private static final MerchantInventoryChecker m_instance;
    
    public static MerchantInventoryChecker getInstance() {
        return MerchantInventoryChecker.m_instance;
    }
    
    @Override
    public int canAddItem(final Inventory inventory, final AbstractMerchantInventoryItem item) {
        final AbstractMerchantInventory merchantInventory = (AbstractMerchantInventory)inventory;
        if (item.getPackType().qty > merchantInventory.getMaximumPack()) {
            return -10;
        }
        return this.itemIsProperType(merchantInventory.getRequiredItemType(), item.getItem());
    }
    
    public int itemIsProperType(final MerchantItemType requiredItemType, final Item item) {
        if (item == null) {
            return -3;
        }
        if (item.getReferenceItem() == null) {
            return -3;
        }
        if (item.getReferenceItem().getItemType() == null) {
            return -3;
        }
        if (requiredItemType != null && !requiredItemType.isValid(item.getReferenceItem().getItemType())) {
            return -3;
        }
        return 0;
    }
    
    @Override
    public int canAddItem(final Inventory restrictedContainer, final AbstractMerchantInventoryItem item, final short position) {
        return this.canAddItem(restrictedContainer, item);
    }
    
    @Override
    public int canReplaceItem(final Inventory restrictedContainer, final AbstractMerchantInventoryItem oldItem, final AbstractMerchantInventoryItem newItem) {
        return this.canAddItem(restrictedContainer, newItem);
    }
    
    @Override
    public int canRemoveItem(final Inventory restrictedContainer, final AbstractMerchantInventoryItem item) {
        return 0;
    }
    
    @Override
    public boolean checkCriterion(final AbstractMerchantInventoryItem item, final EffectUser player, final EffectContext context) {
        return true;
    }
    
    @Override
    public boolean checkCriterion(final Inventory<AbstractMerchantInventoryItem> inventory, final EffectUser player, final EffectContext context) {
        return true;
    }
    
    static {
        m_instance = new MerchantInventoryChecker();
    }
}
