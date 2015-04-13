package com.ankamagames.wakfu.common.game.inventory.reborn.definition;

import gnu.trove.*;

public interface Inventory<I extends InventoryItem>
{
    boolean forEach(TObjectProcedure<I> p0);
}
