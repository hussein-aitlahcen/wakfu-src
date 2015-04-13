package com.ankamagames.wakfu.common.game.world.havenWorld.items;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;

public class PartitionItem implements ModificationItem<PartitionCatalogEntry>
{
    private final PartitionCatalogEntry m_catalogEntry;
    private final int m_patchX;
    private final int m_patchY;
    
    public PartitionItem(final PartitionCatalogEntry catalogEntry, final int patchX, final int patchY) {
        super();
        this.m_catalogEntry = catalogEntry;
        this.m_patchX = patchX;
        this.m_patchY = patchY;
    }
    
    @Override
    public ItemLayer getLayer() {
        return ItemLayer.PARTITION;
    }
    
    @Override
    public Point2i getCell() {
        return new Point2i(this.m_patchX, this.m_patchY);
    }
    
    @Override
    public PartitionCatalogEntry getCatalogEntry() {
        return this.m_catalogEntry;
    }
    
    @Override
    public boolean equals(final Object o) {
        return Helper.equals(this, o);
    }
    
    @Override
    public int hashCode() {
        return Helper.hashCode(this);
    }
    
    public int getPatchX() {
        return this.m_patchX;
    }
    
    public int getPatchY() {
        return this.m_patchY;
    }
    
    @Override
    public long getUid() {
        return 31 * this.m_patchX + this.m_patchY;
    }
}
