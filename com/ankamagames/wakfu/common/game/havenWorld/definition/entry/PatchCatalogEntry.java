package com.ankamagames.wakfu.common.game.havenWorld.definition.entry;

public class PatchCatalogEntry extends HavenWorldCatalogEntry
{
    private final short m_patchId;
    private final int m_cost;
    
    public PatchCatalogEntry(final short id, final short patchId, final int cost, final int categoryId, final short maxQuantity) {
        super(id, categoryId, maxQuantity);
        this.m_patchId = patchId;
        this.m_cost = cost;
    }
    
    public short getPatchId() {
        return this.m_patchId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatchCatalogEntry)) {
            return false;
        }
        final PatchCatalogEntry that = (PatchCatalogEntry)o;
        return this.m_patchId == that.m_patchId;
    }
    
    @Override
    public int hashCode() {
        return this.m_patchId;
    }
    
    @Override
    public String toString() {
        return super.toString() + "m_patchId=" + this.m_patchId;
    }
    
    @Override
    public int getKamaCost() {
        return this.m_cost;
    }
}
