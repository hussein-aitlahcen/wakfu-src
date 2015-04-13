package com.ankamagames.wakfu.common.game.item;

import gnu.trove.*;

public abstract class AbstractFloorItemManager<FloorItem extends AbstractFloorItem>
{
    protected static final short CLOCK_TICK = 1;
    
    public abstract void foreachFloorItem(final TObjectProcedure<FloorItem> p0);
    
    public abstract void silentUnspawnItem(final long p0);
    
    public abstract void addFloorItem(final FloorItem p0);
    
    public abstract FloorItem getFloorItem(final long p0);
}
