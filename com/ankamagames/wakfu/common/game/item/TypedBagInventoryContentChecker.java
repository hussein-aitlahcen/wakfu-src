package com.ankamagames.wakfu.common.game.item;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class TypedBagInventoryContentChecker implements InventoryContentChecker<Item>
{
    private static final Logger m_logger;
    private final int[] m_validItemCategory;
    private final InventoryContentChecker<Item> m_baseChecker;
    
    public TypedBagInventoryContentChecker(final int[] validItemCategory, final InventoryContentChecker<Item> baseChecker) {
        super();
        this.m_validItemCategory = validItemCategory;
        this.m_baseChecker = baseChecker;
    }
    
    @Override
    public int canAddItem(final Inventory<Item> inventory, final Item item) {
        if (!this.accept(item.getType())) {
            return -3;
        }
        return this.m_baseChecker.canAddItem(inventory, item);
    }
    
    @Override
    public int canAddItem(final Inventory<Item> inventory, final Item item, final short position) {
        if (!this.accept(item.getType())) {
            return -3;
        }
        return this.m_baseChecker.canAddItem(inventory, item, position);
    }
    
    @Override
    public int canReplaceItem(final Inventory<Item> inventory, final Item oldItem, final Item newItem) {
        if (!this.accept(newItem.getType())) {
            return -3;
        }
        return this.m_baseChecker.canReplaceItem(inventory, oldItem, newItem);
    }
    
    @Override
    public int canRemoveItem(final Inventory<Item> inventory, final Item item) {
        return this.m_baseChecker.canRemoveItem(inventory, item);
    }
    
    @Override
    public boolean checkCriterion(final Item item, final EffectUser player, final EffectContext context) {
        return this.m_baseChecker.checkCriterion(item, player, context);
    }
    
    @Override
    public boolean checkCriterion(final Inventory<Item> inventory, final EffectUser player, final EffectContext context) {
        return this.m_baseChecker.checkCriterion(inventory, player, context);
    }
    
    private boolean accept(AbstractItemType<AbstractItemType> type) {
        if (this.m_validItemCategory == null || this.m_validItemCategory.length == 0) {
            return true;
        }
        while (!PrimitiveArrays.contains(this.m_validItemCategory, type.getId())) {
            type = (AbstractItemType<AbstractItemType>)type.getParentType();
            if (type == null) {
                return false;
            }
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TypedBagInventoryContentChecker.class);
    }
}
