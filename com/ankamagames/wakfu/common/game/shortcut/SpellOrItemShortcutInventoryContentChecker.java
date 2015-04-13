package com.ankamagames.wakfu.common.game.shortcut;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class SpellOrItemShortcutInventoryContentChecker implements InventoryContentChecker<AbstractShortCutItem>
{
    @Override
    public int canAddItem(final Inventory inventory, final AbstractShortCutItem item) {
        if (item.getType() != ShortCutType.SPELL_LEVEL && item.getType() != ShortCutType.ITEM && item.getType() != ShortCutType.USABLE_REFERENCE_ITEM) {
            return -3;
        }
        return 0;
    }
    
    @Override
    public int canAddItem(final Inventory inventory, final AbstractShortCutItem item, final short position) {
        return this.canAddItem(inventory, item);
    }
    
    @Override
    public int canReplaceItem(final Inventory inventory, final AbstractShortCutItem oldItem, final AbstractShortCutItem newItem) {
        return this.canAddItem(inventory, newItem);
    }
    
    @Override
    public int canRemoveItem(final Inventory inventory, final AbstractShortCutItem item) {
        return this.canAddItem(inventory, item);
    }
    
    @Override
    public boolean checkCriterion(final AbstractShortCutItem item, final EffectUser player, final EffectContext context) {
        return false;
    }
    
    @Override
    public boolean checkCriterion(final Inventory inventory, final EffectUser player, final EffectContext context) {
        return false;
    }
}
