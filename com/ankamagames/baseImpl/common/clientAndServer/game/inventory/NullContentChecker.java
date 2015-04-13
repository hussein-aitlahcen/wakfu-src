package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class NullContentChecker implements InventoryContentChecker
{
    public static final NullContentChecker INSTANCE;
    
    @Override
    public int canAddItem(final Inventory inventory, final InventoryContent item) {
        return 0;
    }
    
    @Override
    public int canAddItem(final Inventory inventory, final InventoryContent item, final short position) {
        return 0;
    }
    
    @Override
    public int canReplaceItem(final Inventory inventory, final InventoryContent oldItem, final InventoryContent newItem) {
        return 0;
    }
    
    @Override
    public int canRemoveItem(final Inventory inventory, final InventoryContent item) {
        return 0;
    }
    
    @Override
    public boolean checkCriterion(final InventoryContent item, final EffectUser player, final EffectContext context) {
        return true;
    }
    
    @Override
    public boolean checkCriterion(final Inventory inventory, final EffectUser player, final EffectContext context) {
        return true;
    }
    
    static {
        INSTANCE = new NullContentChecker();
    }
}
